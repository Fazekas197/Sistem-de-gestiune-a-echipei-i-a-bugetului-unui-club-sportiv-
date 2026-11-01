package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface PlayerWorkflowService {
 PlayerView register(String name, LocalDate birthDate, String position, Integer jerseyNumber, User actor);
 PlayerView update(UUID playerId, String name, LocalDate birthDate, String position, Integer jerseyNumber, User actor);
 PlayerView deactivate(UUID playerId, User actor);
 PlayerView activate(UUID playerId, User actor);
 PlayerView get(UUID playerId, User actor);
 List<PlayerView> list(User actor);
}
