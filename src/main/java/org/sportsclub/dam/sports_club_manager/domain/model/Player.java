package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.InjuryStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;
import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Player {
    private final UUID id;
    @NotBlank private String name;
    @NotNull private LocalDate birthDate;
    @NotBlank private String position;
    private Integer jerseyNumber;
    @NotNull private PlayerStatus status;
    private boolean manuallyInactive;
    @Valid private final List<Contract> contracts = new ArrayList<>();
    @Valid private final List<Injury> injuries = new ArrayList<>();

    public Player(String name, LocalDate birthDate, String position, Integer jerseyNumber) {
        this.id = UUID.randomUUID();
        this.name = name == null ? null : name.trim();
        this.birthDate = birthDate;
        this.position = position == null ? null : position.trim();
        this.jerseyNumber = jerseyNumber;
        this.status = PlayerStatus.ACTIVE;
        recalculateStatus();
    }
    public void updateDetails(String name, LocalDate birthDate, String position, Integer jerseyNumber) {
        this.name = name == null ? null : name.trim();
        this.birthDate = birthDate;
        this.position = position == null ? null : position.trim();
        this.jerseyNumber = jerseyNumber;
    }
    public void addContract(Contract contract) {
        if (contract == null || !id.equals(contract.getPlayerId()))
            throw new BusinessRuleViolationException("Contract must belong to this player.");
        if (contract.getStatus() == ContractStatus.ACTIVE && hasActiveContract())
            throw new BusinessRuleViolationException("A player may have only one active contract.");
        contracts.add(contract);
    }
    public void addInjury(Injury injury) {
        if (injury == null || !id.equals(injury.getPlayerId()))
            throw new BusinessRuleViolationException("Injury must belong to this player.");
        injuries.add(injury);
        recalculateStatus();
    }
    public void recoverInjury(UUID injuryId) {
        Injury injury = injuries.stream().filter(i -> i.getId().equals(injuryId)).findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException("Injury not found for player."));
        injury.recover();
        recalculateStatus();
    }
    public boolean hasActiveInjuries() {
        return injuries.stream().anyMatch(i -> i.getStatus() == InjuryStatus.ACTIVE);
    }
    public boolean hasActiveContract() {
        return contracts.stream().anyMatch(c -> c.getStatus() == ContractStatus.ACTIVE);
    }
    public void deactivate() { manuallyInactive = true; recalculateStatus(); }
    public void activatePlayer() { manuallyInactive = false; recalculateStatus(); }
    public void recalculateStatus() {
        status = manuallyInactive ? PlayerStatus.INACTIVE :
                (hasActiveInjuries() ? PlayerStatus.UNAVAILABLE : PlayerStatus.ACTIVE);
    }
    public boolean isAvailableForSelection() { return status == PlayerStatus.ACTIVE; }
    public UUID getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPosition() { return position; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public PlayerStatus getStatus() { return status; }
    public boolean isManuallyInactive() { return manuallyInactive; }
    public List<Contract> getContracts() { return Collections.unmodifiableList(contracts); }
    public List<Injury> getInjuries() { return Collections.unmodifiableList(injuries); }
}
