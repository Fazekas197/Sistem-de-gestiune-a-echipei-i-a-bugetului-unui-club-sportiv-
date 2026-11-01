package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.SeasonBudget;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.math.BigDecimal;
public interface BudgetWorkflow { SeasonBudget create(Season season, BigDecimal totalAmount, User actor); }
