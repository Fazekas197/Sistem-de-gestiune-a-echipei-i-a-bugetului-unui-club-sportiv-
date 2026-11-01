package org.sportsclub.dam.sports_club_manager.service.computation;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
@Service public class BudgetComputationService implements BudgetComputation {
 public BigDecimal income(SeasonBudget budget) { return budget.getIncome(); }
 public BigDecimal spent(SeasonBudget budget) { return budget.getSpent(); }
  
 public BigDecimal remaining(SeasonBudget budget) { return budget.getTotalAmount().add(income(budget)).subtract(spent(budget)); }
}
