package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.util.UUID;
public interface UserWorkflow { UserView create(String username, String rawPassword, UserRole role, UUID playerId, User actor); UserView find(UUID id, User actor); }
