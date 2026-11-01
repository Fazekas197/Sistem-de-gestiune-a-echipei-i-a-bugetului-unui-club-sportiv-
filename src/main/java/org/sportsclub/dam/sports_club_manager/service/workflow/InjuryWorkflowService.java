package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.model.Injury;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.time.LocalDate;
import java.util.UUID;
public interface InjuryWorkflowService {
 Injury record(UUID playerId,String type,LocalDate startDate,LocalDate expectedReturn,User actor);
 Injury recover(UUID playerId,UUID injuryId,User actor);
}
