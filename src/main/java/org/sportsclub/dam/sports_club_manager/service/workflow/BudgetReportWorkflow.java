package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import org.sportsclub.dam.sports_club_manager.service.computation.BudgetReport;
public interface BudgetReportWorkflow { BudgetReport report(Season season, User actor); }
