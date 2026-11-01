package org.sportsclub.dam.sports_club_manager.service.computation;

import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionCategory;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;

import java.math.BigDecimal;
import java.util.Map;

 


public record BudgetReport(
        Season season,
        BigDecimal initialBudget,
        BigDecimal income,
        BigDecimal spent,
        BigDecimal remaining,
        Map<TransactionCategory, BigDecimal> incomeByCategory,
        Map<TransactionCategory, BigDecimal> expensesByCategory) {
}
