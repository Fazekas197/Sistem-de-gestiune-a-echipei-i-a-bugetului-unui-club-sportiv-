package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.Transaction;
import org.springframework.stereotype.Service;

 
@Service
public class TransactionValidationService {

    public void validate(Transaction transaction) {
        if (transaction == null || !transaction.hasValidFields()) {
            throw new BusinessRuleViolationException(
                    "A transaction requires a budget, type, category, date, and a positive amount.");
        }
    }
}
