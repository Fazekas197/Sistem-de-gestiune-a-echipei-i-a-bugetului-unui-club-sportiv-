package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.exception.DuplicateEntityException;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.factory.PlayerFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.audit.InMemoryAuditService;
import org.sportsclub.dam.sports_club_manager.service.validation.DefaultDomainValidationService;
import org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService;
import org.sportsclub.dam.sports_club_manager.service.workflow.DefaultPlayerWorkflowService;
import org.sportsclub.dam.sports_club_manager.service.workflow.PlayerView;
import org.sportsclub.dam.sports_club_manager.support.player.InMemoryPlayerStore;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerWorkflowTest {
    @Test void coachCanCreatePlayerButDuplicateJerseyIsRejected() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            var workflow = new DefaultPlayerWorkflowService(new InMemoryPlayerStore(), new PlayerFactory(),
                    new DefaultDomainValidationService(factory.getValidator()),
                    new RoleAuthorizationService(), new InMemoryAuditService());
            User coach = new User("coach", "hash", UserRole.COACH, null);
            PlayerView view = workflow.register("Alex", LocalDate.of(2000, 1, 1), "Forward", 9, coach);
            assertEquals("Alex", view.name());
            assertTrue(view.availableForSelection());
            assertThrows(DuplicateEntityException.class,
                    () -> workflow.register("Other", LocalDate.of(2001, 1, 1), "Defender", 9, coach));
        } finally {
            factory.close();
        }
    }
}
