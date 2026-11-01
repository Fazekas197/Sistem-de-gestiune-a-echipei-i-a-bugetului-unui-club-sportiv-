package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.EntityNotFoundException;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService;
import org.sportsclub.dam.sports_club_manager.support.player.PlayerStore;
import org.springframework.stereotype.Service;
@Service public class DefaultPlayerProfileWorkflowService implements PlayerProfileWorkflowService {
 private final PlayerStore players; private final RoleAuthorizationService authorization;
 public DefaultPlayerProfileWorkflowService(PlayerStore players,RoleAuthorizationService authorization) { this.players=players; this.authorization=authorization; }
 @Override public PlayerProfileView ownProfile(User actor) {
  authorization.require(actor,UserRole.PLAYER);
  if (actor.getPlayerId() == null) throw new org.sportsclub.dam.sports_club_manager.domain.exception.UnauthorizedOperationException("Player account is not linked to a player.");
  Player player=players.findById(actor.getPlayerId()).orElseThrow(()->new EntityNotFoundException("Player profile not found."));
  return new PlayerProfileView(player.getId(),player.getName(),player.getBirthDate(),player.getPosition(),player.getJerseyNumber(),player.getStatus(),player.isAvailableForSelection(),player.getContracts());
 }
}
