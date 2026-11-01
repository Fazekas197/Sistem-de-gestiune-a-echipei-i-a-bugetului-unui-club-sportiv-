package org.sportsclub.dam.sports_club_manager.domain.factory;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component
public class UserFactory {
    private final PasswordEncoder passwordEncoder;
    public UserFactory(PasswordEncoder passwordEncoder) { this.passwordEncoder = passwordEncoder; }
    public User create(String username, String rawPassword, UserRole role, UUID playerId) {
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Password is required.");
        return new User(username, passwordEncoder.encode(rawPassword), role, playerId);
    }
}
