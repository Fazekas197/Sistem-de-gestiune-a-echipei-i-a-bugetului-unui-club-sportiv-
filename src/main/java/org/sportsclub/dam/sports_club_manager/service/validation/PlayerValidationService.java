package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.springframework.stereotype.Service;

 



@Service
public class PlayerValidationService {

    public void validate(Player player) {
        if (player == null || player.getName() == null || player.getName().isBlank()
                || player.getBirthDate() == null
                || player.getPosition() == null || player.getPosition().isBlank()) {
            throw new BusinessRuleViolationException(
                    "Player name, birth date, and position are required.");
        }
    }
}
