package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.springframework.stereotype.Service;

 
@Service
public class UserValidationService {

    public void validate(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isBlank()
                || user.getPasswordHash() == null || user.getPasswordHash().isBlank()
                || !user.hasValidAssociation()) {
            throw new BusinessRuleViolationException(
                    "User credentials are required and only PLAYER users may have a player association.");
        }
    }
}
