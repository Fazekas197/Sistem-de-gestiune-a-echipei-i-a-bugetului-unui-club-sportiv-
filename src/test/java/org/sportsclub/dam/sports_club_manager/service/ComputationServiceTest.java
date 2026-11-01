package org.sportsclub.dam.sports_club_manager.service;

import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.computation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ComputationServiceTest {
    @Test void contractCostUsesAnnualSalaryAndTransferFee() {
        Contract contract = new Contract(UUID.randomUUID(), ContractType.PERMANENT, null,
                new BigDecimal("1200"), new BigDecimal("5000"),
                LocalDate.of(2026, 7, 1), null);
        ContractCostComputationService service = new ContractCostComputationService();
        assertEquals(0, service.annualSalary(contract).compareTo(new BigDecimal("14400")));
        assertEquals(0, service.activationCost(contract).compareTo(new BigDecimal("19400")));
    }

    @Test void budgetReportIncludesIncomeAndGroupsBothTransactionTypesByCategory() {
        SeasonBudget budget = new SeasonBudget(new Season(2026, 2027), new BigDecimal("1000"));
        budget.addTransaction(new Transaction(budget.getId(), TransactionType.INCOME, TransactionCategory.OTHER,
                new BigDecimal("250"), LocalDate.now(), "Sponsor"));
        budget.addTransaction(new Transaction(budget.getId(), TransactionType.INCOME, TransactionCategory.TRAVEL,
                new BigDecimal("50"), LocalDate.now(), "Grant"));
        budget.addTransaction(new Transaction(budget.getId(), TransactionType.EXPENSE, TransactionCategory.TRAVEL,
                new BigDecimal("100"), LocalDate.now(), "Travel"));
        Transaction reversedExpense = new Transaction(budget.getId(), TransactionType.EXPENSE,
                TransactionCategory.EQUIPMENT, new BigDecimal("25"), LocalDate.now(), "Reversed purchase");
        budget.addTransaction(reversedExpense);
        budget.reverseTransaction(reversedExpense.getId());

        BudgetReportComputationService reports = new BudgetReportComputationService(new BudgetComputationService());
        BudgetReport report = reports.compute(budget);

        assertEquals(0, report.remaining().compareTo(new BigDecimal("1200")));
        assertEquals(0, report.incomeByCategory().get(TransactionCategory.OTHER).compareTo(new BigDecimal("250")));
        assertEquals(0, report.incomeByCategory().get(TransactionCategory.TRAVEL).compareTo(new BigDecimal("50")));
        assertEquals(0, report.expensesByCategory().get(TransactionCategory.TRAVEL).compareTo(new BigDecimal("100")));
        assertEquals(0, report.expensesByCategory().get(TransactionCategory.EQUIPMENT).compareTo(BigDecimal.ZERO));
        assertThrows(UnsupportedOperationException.class,
                () -> report.incomeByCategory().put(TransactionCategory.OTHER, BigDecimal.ZERO));
    }
}
