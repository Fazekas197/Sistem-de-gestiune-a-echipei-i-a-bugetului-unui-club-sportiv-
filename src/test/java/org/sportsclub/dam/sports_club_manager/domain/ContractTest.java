package org.sportsclub.dam.sports_club_manager.domain;

import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.*;
import org.sportsclub.dam.sports_club_manager.domain.exception.InvalidStateTransitionException;
import org.sportsclub.dam.sports_club_manager.domain.model.Contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {
    @Test void loanRequiresOriginClubAndEndDate() {
        Contract invalid = new Contract(UUID.randomUUID(), ContractType.LOAN, null,
                new BigDecimal("100"), BigDecimal.ZERO, LocalDate.of(2026, 1, 1), null);
        assertFalse(invalid.hasValidTerms());
        Contract valid = new Contract(UUID.randomUUID(), ContractType.LOAN, "Origin FC",
                new BigDecimal("100"), BigDecimal.ZERO, LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1));
        assertTrue(valid.hasValidTerms());
    }

    @Test void expiredContractCannotBeReactivated() {
        Contract contract = new Contract(UUID.randomUUID(), ContractType.PERMANENT, null,
                new BigDecimal("100"), BigDecimal.ZERO, LocalDate.of(2026, 1, 1), null);
        contract.activate();
        contract.expire();
        assertThrows(InvalidStateTransitionException.class, contract::activate);
    }
}
