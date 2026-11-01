package org.sportsclub.dam.sports_club_manager.service;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractType;
import org.sportsclub.dam.sports_club_manager.domain.enums.UserRole;
import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.Contract;
import org.sportsclub.dam.sports_club_manager.domain.model.Injury;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.validation.DefaultDomainValidationService;
import org.sportsclub.dam.sports_club_manager.service.validation.TransactionValidationService;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;
import org.sportsclub.dam.sports_club_manager.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {
    @Test void invalidLoanContractIsRejected() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            DefaultDomainValidationService service = new DefaultDomainValidationService(factory.getValidator());
            Contract invalid = new Contract(UUID.randomUUID(), ContractType.LOAN, null,
                    new BigDecimal("100"), BigDecimal.ZERO, LocalDate.of(2026, 1, 1), null);
            assertThrows(BusinessRuleViolationException.class, () -> service.validate(invalid));
        } finally {
            factory.close();
        }
    }

    @Test void injuryExpectedReturnMustBeAfterStartDate() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            DefaultDomainValidationService service = new DefaultDomainValidationService(factory.getValidator());
            LocalDate start = LocalDate.of(2026, 4, 10);
            Injury invalid = new Injury(UUID.randomUUID(), "knee", start, start);
            assertThrows(BusinessRuleViolationException.class, () -> service.validate(invalid));
        } finally {
            factory.close();
        }
    }

    @Test void userRoleMustMatchPlayerAssociation() {
        var factory = Validation.buildDefaultValidatorFactory();
        try {
            DefaultDomainValidationService service = new DefaultDomainValidationService(factory.getValidator());
            User invalid = new User("coach", "bcrypt-hash", UserRole.COACH, UUID.randomUUID());
            assertThrows(BusinessRuleViolationException.class, () -> service.validate(invalid));
        } finally {
            factory.close();
        }
    }

    @Test void transactionMustHavePositiveAmount() {
        Transaction invalid = new Transaction(UUID.randomUUID(), TransactionType.EXPENSE,
                TransactionCategory.TRAVEL, BigDecimal.ZERO, LocalDate.of(2026, 4, 10), "Invalid amount");
        assertThrows(BusinessRuleViolationException.class,
                () -> new TransactionValidationService().validate(invalid));
    }
}
