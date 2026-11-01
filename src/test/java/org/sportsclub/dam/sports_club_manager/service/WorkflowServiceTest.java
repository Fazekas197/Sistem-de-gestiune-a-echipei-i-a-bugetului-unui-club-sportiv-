package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.factory.SeasonBudgetFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.audit.InMemoryAuditService;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.service.workflow.*;
import org.sportsclub.dam.sports_club_manager.support.budget.InMemorySeasonBudgetStore;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowServiceTest {
    @Test void financialWorkflowRecordsIncomeAndRejectsOverspending() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            var budgetStore = new InMemorySeasonBudgetStore();
            var audit = new InMemoryAuditService();
            var auth = new RoleAuthorizationService();
            var budgetWorkflow = new BudgetWorkflowService(budgetStore, new SeasonBudgetFactory(), auth, audit);
            var validation = new DefaultDomainValidationService(factory.getValidator());
            var transactions = new TransactionWorkflowService(budgetStore, validation, auth, audit);
            User financial = new User("finance", "not-used-in-this-test", UserRole.FINANCIAL, null);
            Season season = new Season(2026, 2027);
            budgetWorkflow.create(season, new BigDecimal("500"), financial);
            transactions.record(season, TransactionType.INCOME, TransactionCategory.OTHER,
                    new BigDecimal("100"), LocalDate.now(), "Sponsor", financial);
            transactions.record(season, TransactionType.EXPENSE, TransactionCategory.TRAVEL,
                    new BigDecimal("600"), LocalDate.now(), "Trip", financial);
            assertEquals(0, budgetStore.findBySeason(season).orElseThrow().getRemaining().compareTo(BigDecimal.ZERO));
            assertEquals(3, audit.findAll().size());
        } finally {
            factory.close();
        }
    }

    @Test void coachCannotCreateBudget() {
        var auth = new RoleAuthorizationService();
        User coach = new User("coach", "hash", UserRole.COACH, null);
        assertThrows(org.sportsclub.dam.sports_club_manager.domain.exception.UnauthorizedOperationException.class,
                () -> auth.require(coach, UserRole.FINANCIAL));
    }
}
