package org.sportsclub.dam.sports_club_manager.domain;

import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;
import org.sportsclub.dam.sports_club_manager.domain.model.Injury;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {
    @Test void seasonRequiresExactlyTwoConsecutiveYears() {
        assertEquals("2026-2027", new Season(2026, 2027).toString());
        assertThrows(IllegalArgumentException.class, () -> new Season(2026, 2026));
        assertThrows(IllegalArgumentException.class, () -> new Season(2026, 2028));
    }

    @Test void playerStatusReflectsMultipleActiveInjuries() {
        Player player = new Player("Alex", LocalDate.of(2000, 1, 1), "Forward", 9);
        Injury first = new Injury(player.getId(), "Knee", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1));
        Injury second = new Injury(player.getId(), "Ankle", LocalDate.of(2026, 1, 5), LocalDate.of(2026, 2, 5));
        player.addInjury(first);
        player.addInjury(second);
        assertEquals(PlayerStatus.UNAVAILABLE, player.getStatus());
        player.recoverInjury(first.getId());
        assertEquals(PlayerStatus.UNAVAILABLE, player.getStatus());
        player.recoverInjury(second.getId());
        assertEquals(PlayerStatus.ACTIVE, player.getStatus());
    }

    @Test void manualInactivityHasPriorityOverInjuryRecovery() {
        Player player = new Player("Alex", LocalDate.of(2000, 1, 1), "Forward", null);
        player.deactivate();
        player.addInjury(new Injury(player.getId(), "Knee", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1)));
        assertEquals(PlayerStatus.INACTIVE, player.getStatus());
        player.activatePlayer();
        assertEquals(PlayerStatus.UNAVAILABLE, player.getStatus());
    }
}
