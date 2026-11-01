package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractStatus;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractType;
import org.sportsclub.dam.sports_club_manager.domain.exception.InvalidStateTransitionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Contract {
    private final UUID id;
    @NotNull private final UUID playerId;
    @NotNull private final ContractType contractType;
    private final String originClub;
    @NotNull @Positive private final BigDecimal monthlySalary;
    @NotNull @PositiveOrZero private final BigDecimal transferFee;
    @NotNull private final LocalDate startDate;
    private final LocalDate endDate;
    @NotNull private ContractStatus status;

    public Contract(UUID playerId, ContractType contractType, String originClub,
                    BigDecimal monthlySalary, BigDecimal transferFee,
                    LocalDate startDate, LocalDate endDate) {
        this.id = UUID.randomUUID();
        this.playerId = playerId;
        this.contractType = contractType;
        this.originClub = normalize(originClub);
        this.monthlySalary = monthlySalary;
        this.transferFee = transferFee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ContractStatus.PROPOSED;
    }
    private static String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
    public boolean hasValidTerms() {
        if (playerId == null || contractType == null || monthlySalary == null ||
                monthlySalary.signum() <= 0 || transferFee == null || transferFee.signum() < 0 ||
                startDate == null) return false;
        if (endDate != null && !endDate.isAfter(startDate)) return false;
        return contractType != ContractType.LOAN || (originClub != null && endDate != null);
    }
    public void activate() {
        if (status != ContractStatus.PROPOSED)
            throw new InvalidStateTransitionException("Only a proposed contract can be activated.");
        status = ContractStatus.ACTIVE;
    }
    public void expire() {
        if (status != ContractStatus.ACTIVE)
            throw new InvalidStateTransitionException("Only an active contract can expire.");
        status = ContractStatus.EXPIRED;
    }
    public void terminate() {
        if (status != ContractStatus.PROPOSED && status != ContractStatus.ACTIVE)
            throw new InvalidStateTransitionException("Only proposed or active contracts can be terminated.");
        status = ContractStatus.TERMINATED;
    }
    public UUID getId() { return id; }
    public UUID getPlayerId() { return playerId; }
    public ContractType getContractType() { return contractType; }
    public String getOriginClub() { return originClub; }
    public BigDecimal getMonthlySalary() { return monthlySalary; }
    public BigDecimal getTransferFee() { return transferFee; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public ContractStatus getStatus() { return status; }
}
