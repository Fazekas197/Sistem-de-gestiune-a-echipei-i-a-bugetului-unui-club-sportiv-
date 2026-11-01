package org.sportsclub.dam.sports_club_manager.service.computation;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import java.math.BigDecimal;
public interface BudgetComputation { BigDecimal income(SeasonBudget budget); BigDecimal spent(SeasonBudget budget); BigDecimal remaining(SeasonBudget budget); }
