package org.sportsclub.dam.sports_club_manager.support.player;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface PlayerStore {
    Player save(Player player);
    Optional<Player> findById(UUID id);
    List<Player> findAll();
    boolean existsByJerseyNumber(Integer jerseyNumber, UUID exceptPlayerId);
}
