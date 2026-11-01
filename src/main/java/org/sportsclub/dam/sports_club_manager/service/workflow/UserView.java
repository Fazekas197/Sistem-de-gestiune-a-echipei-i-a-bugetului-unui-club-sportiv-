package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import java.util.UUID;
 
public record UserView(UUID id, String username, UserRole role, UUID playerId) { }
