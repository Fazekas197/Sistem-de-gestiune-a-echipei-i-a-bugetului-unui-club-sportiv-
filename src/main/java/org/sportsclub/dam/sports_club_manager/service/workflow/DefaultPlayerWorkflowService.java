package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.*;
import org.sportsclub.dam.sports_club_manager.domain.factory.PlayerFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.audit.*;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.support.player.PlayerStore;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Service public class DefaultPlayerWorkflowService implements PlayerWorkflowService {
 private final PlayerStore store; private final PlayerFactory factory; private final DomainValidationService validation; private final RoleAuthorizationService authorization; private final AuditService audit;
 public DefaultPlayerWorkflowService(PlayerStore store, PlayerFactory factory, DomainValidationService validation, RoleAuthorizationService authorization, AuditService audit) { this.store=store; this.factory=factory; this.validation=validation; this.authorization=authorization; this.audit=audit; }
 @Override public PlayerView register(String name, LocalDate birthDate, String position, Integer jerseyNumber, User actor) {
  authorization.require(actor,UserRole.COACH,UserRole.MANAGER); Player p=factory.create(name,birthDate,position,jerseyNumber); validation.validate(p);
  if(store.existsByJerseyNumber(jerseyNumber,null)) throw new DuplicateEntityException("Jersey number is already assigned.");
  store.save(p); audit.record(actor,AuditAction.PLAYER_CREATED,"Player",p.getId(),"Player registered"); return view(p);
 }
 @Override public PlayerView update(UUID id,String name,LocalDate birthDate,String position,Integer jerseyNumber,User actor) {
  authorization.require(actor,UserRole.COACH,UserRole.MANAGER); Player p=load(id);
  if(store.existsByJerseyNumber(jerseyNumber,id)) throw new DuplicateEntityException("Jersey number is already assigned.");
  Player candidate=factory.create(name,birthDate,position,jerseyNumber); validation.validate(candidate);
  p.updateDetails(name,birthDate,position,jerseyNumber);
  store.save(p); audit.record(actor,AuditAction.PLAYER_UPDATED,"Player",p.getId(),"Player details updated"); return view(p);
 }
 @Override public PlayerView deactivate(UUID id,User actor) { authorization.require(actor,UserRole.COACH,UserRole.MANAGER); Player p=load(id); p.deactivate(); store.save(p); audit.record(actor,AuditAction.PLAYER_DEACTIVATED,"Player",id,"Player deactivated"); return view(p); }
 @Override public PlayerView activate(UUID id,User actor) { authorization.require(actor,UserRole.COACH,UserRole.MANAGER); Player p=load(id); p.activatePlayer(); store.save(p); audit.record(actor,AuditAction.PLAYER_ACTIVATED,"Player",id,"Player activated"); return view(p); }
 @Override public PlayerView get(UUID id,User actor) { authorization.require(actor,UserRole.COACH,UserRole.MANAGER,UserRole.FINANCIAL,UserRole.DOCTOR); return view(store.findById(id).orElseThrow(()->new EntityNotFoundException("Player not found: "+id))); }
 @Override public List<PlayerView> list(User actor) { authorization.require(actor,UserRole.COACH,UserRole.MANAGER,UserRole.FINANCIAL,UserRole.DOCTOR); return store.findAll().stream().map(this::view).toList(); }
 private Player load(UUID id) { return store.findById(id).orElseThrow(()->new EntityNotFoundException("Player not found: "+id)); }
 private PlayerView view(Player p) { return new PlayerView(p.getId(),p.getName(),p.getBirthDate(),p.getPosition(),p.getJerseyNumber(),p.getStatus(),p.isAvailableForSelection(),p.hasActiveInjuries()); }
}
