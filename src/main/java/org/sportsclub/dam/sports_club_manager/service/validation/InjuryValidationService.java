package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.Injury;
import org.springframework.stereotype.Service;

 
@Service
public class InjuryValidationService {

    public void validate(Injury injury) {
        if (injury == null || !injury.hasValidDates()) {
            throw new BusinessRuleViolationException(
                    "Injury requires a player, type, start date, and an expected return date after the start date.");
        }
    }
}
