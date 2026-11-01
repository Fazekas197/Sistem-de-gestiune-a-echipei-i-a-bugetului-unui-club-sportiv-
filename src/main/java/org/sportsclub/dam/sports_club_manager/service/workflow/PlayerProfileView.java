package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;
import org.sportsclub.dam.sports_club_manager.domain.model.Contract;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public record PlayerProfileView(UUID playerId, String name, LocalDate birthDate, String position, Integer jerseyNumber, PlayerStatus status, boolean availableForSelection, List<Contract> contracts) { }
