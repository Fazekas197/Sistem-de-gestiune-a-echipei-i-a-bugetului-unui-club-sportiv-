package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.exception.*;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.audit.*;
import org.sportsclub.dam.sports_club_manager.service.computation.ContractCostComputationService;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.support.player.PlayerStore;
import org.sportsclub.dam.sports_club_manager.support.budget.SeasonBudgetStore;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Service public class DefaultContractWorkflowService implements ContractWorkflowService {
 private final PlayerStore players; private final SeasonBudgetStore budgets; private final DomainValidationService validation; private final RoleAuthorizationService authorization; private final ContractCostComputationService costs; private final AuditService audit;
 public DefaultContractWorkflowService(PlayerStore players,SeasonBudgetStore budgets,DomainValidationService validation,RoleAuthorizationService authorization,ContractCostComputationService costs,AuditService audit) { this.players=players; this.budgets=budgets; this.validation=validation; this.authorization=authorization; this.costs=costs; this.audit=audit; }
 private Player player(UUID id) { return players.findById(id).orElseThrow(()->new EntityNotFoundException("Player not found: "+id)); }
 private Contract contract(Player p,UUID id) { return p.getContracts().stream().filter(c->c.getId().equals(id)).findFirst().orElseThrow(()->new EntityNotFoundException("Contract not found: "+id)); }
 @Override public Contract propose(UUID playerId,ContractType type,String origin,BigDecimal salary,BigDecimal fee,LocalDate start,LocalDate end,User actor) {
  authorization.require(actor,UserRole.MANAGER); Player p=player(playerId); Contract c=new Contract(playerId,type,origin,salary,fee,start,end); validation.validate(c); p.addContract(c); players.save(p); audit.record(actor,AuditAction.CONTRACT_PROPOSED,"Contract",c.getId(),"Contract proposal created"); return c;
 }
 @Override public Contract activate(UUID playerId,UUID contractId,Season season,User actor) {
  authorization.require(actor,UserRole.MANAGER); Player p=player(playerId); Contract c=contract(p,contractId); validation.validate(c);
  if (p.getContracts().stream().anyMatch(other->!other.getId().equals(c.getId()) && other.getStatus()==ContractStatus.ACTIVE)) throw new BusinessRuleViolationException("Player already has an active contract.");
  SeasonBudget budget=budgets.findBySeason(season).orElseThrow(()->new EntityNotFoundException("No budget defined for season "+season));
  BigDecimal cost=costs.activationCost(c); if(cost.compareTo(budget.getRemaining())>0) throw new InsufficientBudgetException("Annual salary plus transfer fee exceeds remaining budget.");
  c.activate(); players.save(p); audit.record(actor,AuditAction.CONTRACT_ACTIVATED,"Contract",c.getId(),"Activation cost checked: "+cost); return c;
 }
 @Override public Contract expire(UUID playerId,UUID contractId,User actor) { authorization.require(actor,UserRole.MANAGER); Player p=player(playerId); Contract c=contract(p,contractId); c.expire(); players.save(p); audit.record(actor,AuditAction.CONTRACT_EXPIRED,"Contract",c.getId(),"Contract expired"); return c; }
 @Override public Contract terminate(UUID playerId,UUID contractId,User actor) { authorization.require(actor,UserRole.MANAGER); Player p=player(playerId); Contract c=contract(p,contractId); c.terminate(); players.save(p); audit.record(actor,AuditAction.CONTRACT_TERMINATED,"Contract",c.getId(),"Contract terminated"); return c; }
 @Override public java.util.List<Contract> findContracts(UUID playerId,User actor) { authorization.require(actor,UserRole.MANAGER,UserRole.FINANCIAL); return java.util.List.copyOf(player(playerId).getContracts()); }
}
