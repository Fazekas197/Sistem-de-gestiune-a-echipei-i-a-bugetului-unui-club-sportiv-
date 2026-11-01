package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
@Service public class BudgetWorkflowService implements BudgetWorkflow {
 private final org.sportsclub.dam.sports_club_manager.support.budget.SeasonBudgetStore store;
 private final org.sportsclub.dam.sports_club_manager.domain.factory.SeasonBudgetFactory factory;
 private final org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService authorization;
 private final org.sportsclub.dam.sports_club_manager.service.audit.AuditService audit;
 public BudgetWorkflowService(org.sportsclub.dam.sports_club_manager.support.budget.SeasonBudgetStore store, org.sportsclub.dam.sports_club_manager.domain.factory.SeasonBudgetFactory factory, org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService authorization, org.sportsclub.dam.sports_club_manager.service.audit.AuditService audit) { this.store=store; this.factory=factory; this.authorization=authorization; this.audit=audit; }
 public SeasonBudget create(Season season,BigDecimal totalAmount,User actor) {
  authorization.require(actor,org.sportsclub.dam.sports_club_manager.domain.enums.UserRole.FINANCIAL);
  if(store.findBySeason(season).isPresent()) throw new org.sportsclub.dam.sports_club_manager.domain.exception.DuplicateEntityException("A budget already exists for season "+season);
  SeasonBudget budget=factory.create(season,totalAmount); store.save(budget); audit.record(actor,org.sportsclub.dam.sports_club_manager.service.audit.AuditAction.BUDGET_CREATED,"SeasonBudget",budget.getId(),"Budget created for "+season); return budget;
 }
}
