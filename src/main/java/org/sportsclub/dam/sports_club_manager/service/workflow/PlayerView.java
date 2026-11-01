package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;
import java.time.LocalDate;
import java.util.UUID;
 
public record PlayerView(UUID id, String name, LocalDate birthDate, String position,
                         Integer jerseyNumber, PlayerStatus status,
                         boolean availableForSelection, boolean hasActiveInjuries) { }
