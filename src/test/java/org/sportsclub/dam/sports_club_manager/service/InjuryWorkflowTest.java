package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.sportsclub.dam.sports_club_manager.service.audit.InMemoryAuditService;
import org.sportsclub.dam.sports_club_manager.service.validation.*;
import org.sportsclub.dam.sports_club_manager.service.workflow.DefaultInjuryWorkflowService;
import org.sportsclub.dam.sports_club_manager.support.player.InMemoryPlayerStore;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InjuryWorkflowTest {
    @Test void doctorCanRecordAndRecoverInjuryAndPlayerAvailabilityIsRecomputed() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            var players = new InMemoryPlayerStore();
            var validation = new DefaultDomainValidationService(factory.getValidator());
            var workflow = new DefaultInjuryWorkflowService(players, validation, new RoleAuthorizationService(), new InMemoryAuditService());
            Player player = new Player("Alex", LocalDate.of(2000, 1, 1), "Defender", 4);
            players.save(player);
            User doctor = new User("doctor", "hash", UserRole.DOCTOR, null);
            Injury injury = workflow.record(player.getId(), "Ankle", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1), doctor);
            assertEquals(PlayerStatus.UNAVAILABLE, player.getStatus());
            workflow.recover(player.getId(), injury.getId(), doctor);
            assertEquals(PlayerStatus.ACTIVE, player.getStatus());
        } finally {
            factory.close();
        }
    }
}
