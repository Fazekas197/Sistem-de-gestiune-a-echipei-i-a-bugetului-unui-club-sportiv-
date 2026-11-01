package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.EntityNotFoundException;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.computation.BudgetReport;
import org.sportsclub.dam.sports_club_manager.service.computation.BudgetReportComputationService;
import org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService;
import org.sportsclub.dam.sports_club_manager.support.budget.SeasonBudgetStore;
import org.springframework.stereotype.Service;
@Service public class BudgetReportWorkflowService implements BudgetReportWorkflow {
 private final SeasonBudgetStore store; private final BudgetReportComputationService computation; private final RoleAuthorizationService authorization;
 public BudgetReportWorkflowService(SeasonBudgetStore store,BudgetReportComputationService computation,RoleAuthorizationService authorization) { this.store=store; this.computation=computation; this.authorization=authorization; }
 public BudgetReport report(Season season,User actor) { authorization.require(actor,UserRole.MANAGER,UserRole.FINANCIAL); return computation.compute(store.findBySeason(season).orElseThrow(()->new EntityNotFoundException("Budget not found for season "+season))); }
}
