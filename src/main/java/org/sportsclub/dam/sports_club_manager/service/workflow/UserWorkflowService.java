package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.DuplicateEntityException;
import org.sportsclub.dam.sports_club_manager.domain.factory.UserFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.audit.*;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.support.user.UserStore;
import org.springframework.stereotype.Service;
import java.util.UUID;
@Service public class UserWorkflowService implements UserWorkflow {
 private final UserStore store; private final UserFactory factory; private final DomainValidationService validation; private final RoleAuthorizationService authorization; private final AuditService audit;
 public UserWorkflowService(UserStore store,UserFactory factory,DomainValidationService validation,RoleAuthorizationService authorization,AuditService audit) { this.store=store; this.factory=factory; this.validation=validation; this.authorization=authorization; this.audit=audit; }
 public UserView create(String username,String rawPassword,UserRole role,UUID playerId,User actor) {
  authorization.require(actor,UserRole.MANAGER); if(store.existsByUsername(username)) throw new DuplicateEntityException("Username already exists."); User user=factory.create(username,rawPassword,role,playerId); validation.validate(user); store.save(user); audit.record(actor,AuditAction.USER_CREATED,"User",user.getId(),"User created: "+username); return view(user);
 }
 public UserView find(UUID id,User actor) {
  if(actor==null || (actor.getRole()!=UserRole.MANAGER && !actor.getId().equals(id))) throw new org.sportsclub.dam.sports_club_manager.domain.exception.UnauthorizedOperationException("User profile access denied.");
  return view(store.findById(id).orElseThrow(()->new org.sportsclub.dam.sports_club_manager.domain.exception.EntityNotFoundException("User not found: "+id)));
 }
 private UserView view(User user) { return new UserView(user.getId(),user.getUsername(),user.getRole(),user.getPlayerId()); }
}
