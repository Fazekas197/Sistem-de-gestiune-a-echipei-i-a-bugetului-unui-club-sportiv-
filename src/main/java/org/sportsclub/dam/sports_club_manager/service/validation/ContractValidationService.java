package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.Contract;
import org.springframework.stereotype.Service;

 
@Service
public class ContractValidationService {

    public void validate(Contract contract) {
        if (contract == null || !contract.hasValidTerms()) {
            throw new BusinessRuleViolationException(
                    "Contract terms are invalid. Check salary, transfer fee, dates, and loan details.");
        }
    }
}
