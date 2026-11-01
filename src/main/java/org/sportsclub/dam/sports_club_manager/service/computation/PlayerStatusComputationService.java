package org.sportsclub.dam.sports_club_manager.service.computation;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.springframework.stereotype.Service;
@Service public class PlayerStatusComputationService implements PlayerStatusComputation {
 public PlayerStatus compute(Player player) { if (player.isManuallyInactive()) return PlayerStatus.INACTIVE; return player.hasActiveInjuries() ? PlayerStatus.UNAVAILABLE : PlayerStatus.ACTIVE; }
}
