package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.factory.SeasonBudgetFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.audit.InMemoryAuditService;
import org.sportsclub.dam.sports_club_manager.service.computation.ContractCostComputationService;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.service.workflow.*;
import org.sportsclub.dam.sports_club_manager.support.budget.InMemorySeasonBudgetStore;
import org.sportsclub.dam.sports_club_manager.support.player.InMemoryPlayerStore;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractWorkflowTest {
    @Test void managerCanActivateContractOnlyWithinRemainingBudget() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            var playerStore = new InMemoryPlayerStore();
            var budgetStore = new InMemorySeasonBudgetStore();
            var validation = new DefaultDomainValidationService(factory.getValidator());
            var auth = new RoleAuthorizationService();
            var audit = new InMemoryAuditService();
            var player = new Player("Alex", LocalDate.of(2000, 1, 1), "Forward", 10);
            playerStore.save(player);
            Season season = new Season(2026, 2027);
            var budgetWorkflow = new BudgetWorkflowService(budgetStore, new SeasonBudgetFactory(), auth, audit);
            budgetWorkflow.create(season, new BigDecimal("15000"), new User("finance", "hash", UserRole.FINANCIAL, null));
            var contracts = new DefaultContractWorkflowService(playerStore, budgetStore, validation, auth,
                    new ContractCostComputationService(), audit);
            User manager = new User("manager", "hash", UserRole.MANAGER, null);
            Contract contract = contracts.propose(player.getId(), ContractType.PERMANENT, null,
                    new BigDecimal("1000"), new BigDecimal("3000"), LocalDate.of(2026, 7, 1), null, manager);
            contracts.activate(player.getId(), contract.getId(), season, manager);
            assertEquals(ContractStatus.ACTIVE, contract.getStatus());
            assertEquals(3, audit.findAll().size());
        } finally {
            factory.close();
        }
    }
}
