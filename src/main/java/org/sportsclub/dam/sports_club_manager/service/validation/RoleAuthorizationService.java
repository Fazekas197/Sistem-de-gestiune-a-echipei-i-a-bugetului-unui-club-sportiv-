package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.UnauthorizedOperationException;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleAuthorizationService {
    public void require(User actor, UserRole... allowedRoles) {
        if (actor == null || actor.getRole() == null ||
                Arrays.stream(allowedRoles).noneMatch(role -> role == actor.getRole())) {
            throw new UnauthorizedOperationException("The current user is not allowed to perform this operation.");
        }
    }
}
