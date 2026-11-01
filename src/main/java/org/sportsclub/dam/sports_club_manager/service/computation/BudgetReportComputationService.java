package org.sportsclub.dam.sports_club_manager.service.computation;

import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.sportsclub.dam.sports_club_manager.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

 
@Service
public class BudgetReportComputationService implements BudgetReportComputation {
    private final BudgetComputationService computation;

    public BudgetReportComputationService(BudgetComputationService computation) {
        this.computation = computation;
    }

    @Override
    public BudgetReport compute(SeasonBudget budget) {
        Map<TransactionCategory, BigDecimal> incomeByCategory = emptyCategoryTotals();
        Map<TransactionCategory, BigDecimal> expensesByCategory = emptyCategoryTotals();

        for (Transaction transaction : budget.getTransactions()) {
            if (transaction.getStatus() != TransactionStatus.ACTIVE) {
                continue;
            }

            Map<TransactionCategory, BigDecimal> target =
                    transaction.getType() == TransactionType.INCOME
                            ? incomeByCategory : expensesByCategory;
            target.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add);
        }

        return new BudgetReport(
                budget.getSeason(),
                budget.getTotalAmount(),
                computation.income(budget),
                computation.spent(budget),
                computation.remaining(budget),
                Collections.unmodifiableMap(incomeByCategory),
                Collections.unmodifiableMap(expensesByCategory));
    }

    private Map<TransactionCategory, BigDecimal> emptyCategoryTotals() {
        Map<TransactionCategory, BigDecimal> totals = new EnumMap<>(TransactionCategory.class);
        for (TransactionCategory category : TransactionCategory.values()) {
            totals.put(category, BigDecimal.ZERO);
        }
        return totals;
    }
}
