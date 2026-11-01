package org.sportsclub.dam.sports_club_manager.support.user;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.util.Optional;
import java.util.UUID;
public interface UserStore {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
