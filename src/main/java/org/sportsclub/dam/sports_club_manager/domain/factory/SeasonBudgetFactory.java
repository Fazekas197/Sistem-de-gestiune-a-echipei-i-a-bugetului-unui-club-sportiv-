package org.sportsclub.dam.sports_club_manager.domain.factory;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
@Component
public class SeasonBudgetFactory {
    public SeasonBudget create(Season season, BigDecimal totalAmount) {
        return new SeasonBudget(season, totalAmount);
    }
}
