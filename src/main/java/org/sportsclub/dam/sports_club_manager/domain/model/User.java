package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;

import java.util.UUID;

public class User {
    private final UUID id;
    @NotBlank private final String username;
    @NotBlank private final String passwordHash;
    @NotNull private final UserRole role;
    private final UUID playerId;

    public User(String username, String passwordHash, UserRole role, UUID playerId) {
        this.id = UUID.randomUUID();
        this.username = username == null ? null : username.trim();
        this.passwordHash = passwordHash;
        this.role = role;
        this.playerId = playerId;
    }
    public boolean hasValidAssociation() {
        return role != null && ((role == UserRole.PLAYER && playerId != null) ||
                (role != UserRole.PLAYER && playerId == null));
    }
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }
    public UUID getPlayerId() { return playerId; }
    @Override public String toString() { return "User{id=" + id + ", username='" + username + "', role=" + role + "}"; }
}
