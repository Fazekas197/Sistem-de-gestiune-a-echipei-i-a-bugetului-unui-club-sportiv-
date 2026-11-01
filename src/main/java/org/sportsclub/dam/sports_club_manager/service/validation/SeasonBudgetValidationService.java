package org.sportsclub.dam.sports_club_manager.service.validation;

import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

 
@Service
public class SeasonBudgetValidationService {

    public void validate(SeasonBudget budget) {
        if (budget == null || budget.getSeason() == null
                || budget.getTotalAmount() == null
                || budget.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleViolationException(
                    "A season budget requires a season and a positive initial amount.");
        }
    }
}
