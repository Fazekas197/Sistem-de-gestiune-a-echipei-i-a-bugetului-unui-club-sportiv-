package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.factory.UserFactory;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.audit.InMemoryAuditService;
import org.sportsclub.dam.sports_club_manager.service.validation.DefaultDomainValidationService;
import org.sportsclub.dam.sports_club_manager.service.validation.RoleAuthorizationService;
import org.sportsclub.dam.sports_club_manager.service.workflow.UserWorkflowService;
import org.sportsclub.dam.sports_club_manager.support.user.InMemoryUserStore;

import static org.junit.jupiter.api.Assertions.*;

class UserWorkflowTest {
    @Test void passwordsAreStoredAsBcryptAndWorkflowReturnsSanitizedView() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            var encoder = new BCryptPasswordEncoder();
            var store = new InMemoryUserStore();
            var workflow = new UserWorkflowService(store, new UserFactory(encoder),
                    new DefaultDomainValidationService(factory.getValidator()),
                    new RoleAuthorizationService(), new InMemoryAuditService());
            User manager = new User("manager", "not-used", UserRole.MANAGER, null);
            var view = workflow.create("finance", "secret123", UserRole.FINANCIAL, null, manager);
            var stored = store.findByUsername("FINANCE").orElseThrow();
            assertNotEquals("secret123", stored.getPasswordHash());
            assertTrue(encoder.matches("secret123", stored.getPasswordHash()));
            assertEquals("finance", view.username());
        } finally {
            factory.close();
        }
    }
}
