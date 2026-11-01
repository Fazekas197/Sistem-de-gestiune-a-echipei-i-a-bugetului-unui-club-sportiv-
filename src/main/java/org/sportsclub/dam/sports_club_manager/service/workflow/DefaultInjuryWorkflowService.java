package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.EntityNotFoundException;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.audit.*;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.support.player.PlayerStore;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.UUID;
@Service public class DefaultInjuryWorkflowService implements InjuryWorkflowService {
 private final PlayerStore players; private final DomainValidationService validation; private final RoleAuthorizationService authorization; private final AuditService audit;
 public DefaultInjuryWorkflowService(PlayerStore players,DomainValidationService validation,RoleAuthorizationService authorization,AuditService audit) { this.players=players; this.validation=validation; this.authorization=authorization; this.audit=audit; }
 @Override public Injury record(UUID playerId,String type,LocalDate start,LocalDate expected,User actor) {
  authorization.require(actor,UserRole.DOCTOR); Player p=players.findById(playerId).orElseThrow(()->new EntityNotFoundException("Player not found: "+playerId)); Injury injury=new Injury(playerId,type,start,expected); validation.validate(injury); p.addInjury(injury); players.save(p); audit.record(actor,AuditAction.INJURY_RECORDED,"Injury",injury.getId(),"Injury recorded for player "+playerId); return injury;
 }
 @Override public Injury recover(UUID playerId,UUID injuryId,User actor) {
  authorization.require(actor,UserRole.DOCTOR); Player p=players.findById(playerId).orElseThrow(()->new EntityNotFoundException("Player not found: "+playerId)); p.recoverInjury(injuryId); players.save(p);
  Injury injury=p.getInjuries().stream().filter(i->i.getId().equals(injuryId)).findFirst().orElseThrow(); audit.record(actor,AuditAction.INJURY_RECOVERED,"Injury",injuryId,"Injury marked recovered"); return injury;
 }
}
