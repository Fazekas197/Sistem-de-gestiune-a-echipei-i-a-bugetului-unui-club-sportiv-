package org.sportsclub.dam.sports_club_manager.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

 


@Service
public class DefaultDomainValidationService implements DomainValidationService {
    private final Validator validator;
    private final PlayerValidationService playerValidation;
    private final ContractValidationService contractValidation;
    private final InjuryValidationService injuryValidation;
    private final SeasonBudgetValidationService budgetValidation;
    private final TransactionValidationService transactionValidation;
    private final UserValidationService userValidation;

    @Autowired
    public DefaultDomainValidationService(
            Validator validator,
            PlayerValidationService playerValidation,
            ContractValidationService contractValidation,
            InjuryValidationService injuryValidation,
            SeasonBudgetValidationService budgetValidation,
            TransactionValidationService transactionValidation,
            UserValidationService userValidation) {
        this.validator = validator;
        this.playerValidation = playerValidation;
        this.contractValidation = contractValidation;
        this.injuryValidation = injuryValidation;
        this.budgetValidation = budgetValidation;
        this.transactionValidation = transactionValidation;
        this.userValidation = userValidation;
    }

     



    public DefaultDomainValidationService(Validator validator) {
        this(validator, new PlayerValidationService(), new ContractValidationService(),
                new InjuryValidationService(), new SeasonBudgetValidationService(),
                new TransactionValidationService(), new UserValidationService());
    }

    @Override
    public void validate(Object target) {
        if (target == null) {
            throw new BusinessRuleViolationException("Validated object cannot be null.");
        }

        Set<ConstraintViolation<Object>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            String details = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .sorted()
                    .collect(Collectors.joining("; "));
            throw new BusinessRuleViolationException("Validation failed: " + details);
        }

        if (target instanceof Player player) {
            playerValidation.validate(player);
        } else if (target instanceof Contract contract) {
            contractValidation.validate(contract);
        } else if (target instanceof Injury injury) {
            injuryValidation.validate(injury);
        } else if (target instanceof SeasonBudget budget) {
            budgetValidation.validate(budget);
        } else if (target instanceof Transaction transaction) {
            transactionValidation.validate(transaction);
        } else if (target instanceof User user) {
            userValidation.validate(user);
        }
    }
}
