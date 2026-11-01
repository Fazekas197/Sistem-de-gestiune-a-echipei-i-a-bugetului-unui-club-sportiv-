package org.sportsclub.dam.sports_club_manager.support.budget;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface SeasonBudgetStore {
    SeasonBudget save(SeasonBudget budget);
    Optional<SeasonBudget> findById(UUID id);
    Optional<SeasonBudget> findBySeason(Season season);
    List<SeasonBudget> findAll();
}
