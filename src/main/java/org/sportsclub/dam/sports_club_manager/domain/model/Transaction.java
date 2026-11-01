package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;
import org.sportsclub.dam.sports_club_manager.domain.exception.InvalidStateTransitionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    @NotNull private final UUID seasonBudgetId;
    @NotNull private final TransactionType type;
    @NotNull private final TransactionCategory category;
    @NotNull @Positive private final BigDecimal amount;
    @NotNull private final LocalDate date;
    private final String description;
    @NotNull private TransactionStatus status;

    public Transaction(UUID seasonBudgetId, TransactionType type, TransactionCategory category,
                       BigDecimal amount, LocalDate date, String description) {
        this.id = UUID.randomUUID();
        this.seasonBudgetId = seasonBudgetId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description == null ? "" : description.trim();
        this.status = TransactionStatus.ACTIVE;
    }
    public boolean hasValidFields() {
        return seasonBudgetId != null && type != null && category != null && amount != null && amount.signum() > 0 && date != null;
    }
    public void reverse() {
        if (status != TransactionStatus.ACTIVE)
            throw new InvalidStateTransitionException("Only an active transaction can be reversed.");
        status = TransactionStatus.REVERSED;
    }
    public UUID getId() { return id; }
    public UUID getSeasonBudgetId() { return seasonBudgetId; }
    public TransactionType getType() { return type; }
    public TransactionCategory getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public TransactionStatus getStatus() { return status; }
}
