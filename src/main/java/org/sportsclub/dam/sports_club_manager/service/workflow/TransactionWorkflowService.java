package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Service public class TransactionWorkflowService implements TransactionWorkflow {
 private final org.sportsclub.dam.sports_club_manager.support.budget.SeasonBudgetStore store;
 private final org.sportsclub.dam.sports_club_manager.service.validation.DomainValidationService validation;
 private final org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService authorization;
 private final org.sportsclub.dam.sports_club_manager.service.audit.AuditService audit;
 public TransactionWorkflowService(org.sportsclub.dam.sports_club_manager.support.budget.SeasonBudgetStore store,org.sportsclub.dam.sports_club_manager.service.validation.DomainValidationService validation,org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService authorization,org.sportsclub.dam.sports_club_manager.service.audit.AuditService audit) { this.store=store; this.validation=validation; this.authorization=authorization; this.audit=audit; }
 private SeasonBudget budget(Season season) { return store.findBySeason(season).orElseThrow(()->new org.sportsclub.dam.sports_club_manager.domain.exception.EntityNotFoundException("Budget not found for season "+season)); }
 public Transaction record(Season season,TransactionType type,TransactionCategory category,BigDecimal amount,LocalDate date,String description,User actor) {
  authorization.require(actor,UserRole.FINANCIAL); SeasonBudget b=budget(season); Transaction t=new Transaction(b.getId(),type,category,amount,date,description); validation.validate(t); b.addTransaction(t); store.save(b); audit.record(actor,org.sportsclub.dam.sports_club_manager.service.audit.AuditAction.TRANSACTION_RECORDED,"Transaction",t.getId(),type+" "+amount); return t;
 }
 public Transaction reverse(Season season,UUID transactionId,User actor) {
  authorization.require(actor,UserRole.FINANCIAL); SeasonBudget b=budget(season); b.reverseTransaction(transactionId); store.save(b);
  Transaction t=b.getTransactions().stream().filter(x->x.getId().equals(transactionId)).findFirst().orElseThrow(); audit.record(actor,org.sportsclub.dam.sports_club_manager.service.audit.AuditAction.TRANSACTION_REVERSED,"Transaction",transactionId,"Transaction reversed"); return t;
 }
}
