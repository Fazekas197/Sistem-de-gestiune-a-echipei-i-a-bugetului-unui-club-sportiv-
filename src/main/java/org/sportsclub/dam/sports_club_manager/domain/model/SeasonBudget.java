package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;
import org.sportsclub.dam.sports_club_manager.domain.exception.InsufficientBudgetException;
import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SeasonBudget {
    private final UUID id;
    @NotNull private final Season season;
    @NotNull @Positive private final BigDecimal totalAmount;
    @Valid private final List<Transaction> transactions = new ArrayList<>();

    public SeasonBudget(Season season, BigDecimal totalAmount) {
        if (season == null) throw new IllegalArgumentException("Season is required.");
        if (totalAmount == null || totalAmount.signum() <= 0)
            throw new IllegalArgumentException("Budget total must be positive.");
        this.id = UUID.randomUUID();
        this.season = season;
        this.totalAmount = totalAmount;
    }
    public void addTransaction(Transaction transaction) {
        if (transaction == null || !id.equals(transaction.getSeasonBudgetId()))
            throw new BusinessRuleViolationException("Transaction must belong to this season budget.");
        if (!transaction.hasValidFields())
            throw new BusinessRuleViolationException("Transaction fields are invalid.");
        if (transactions.stream().anyMatch(existing -> existing.getId().equals(transaction.getId())))
            throw new BusinessRuleViolationException("Transaction is already part of this budget.");
        if (transaction.getStatus() != TransactionStatus.ACTIVE)
            throw new BusinessRuleViolationException("Only active transactions can be added.");
        if (transaction.getType() == TransactionType.EXPENSE &&
                transaction.getAmount().compareTo(getRemaining()) > 0)
            throw new InsufficientBudgetException("Expense exceeds the remaining season budget.");
        transactions.add(transaction);
    }
    public void reverseTransaction(UUID transactionId) {
        Transaction transaction = transactions.stream().filter(t -> t.getId().equals(transactionId)).findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException("Transaction not found in this budget."));
        if (transaction.getType() == TransactionType.INCOME && getRemaining().subtract(transaction.getAmount()).signum() < 0)
            throw new InsufficientBudgetException("Cannot reverse income because existing expenses would exceed the remaining budget.");
        transaction.reverse();
    }
    public BigDecimal getIncome() { return sum(TransactionType.INCOME); }
    public BigDecimal getSpent() { return sum(TransactionType.EXPENSE); }
    public BigDecimal getRemaining() { return totalAmount.add(getIncome()).subtract(getSpent()); }
    private BigDecimal sum(TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.ACTIVE && t.getType() == type)
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public UUID getId() { return id; }
    public Season getSeason() { return season; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }
}
