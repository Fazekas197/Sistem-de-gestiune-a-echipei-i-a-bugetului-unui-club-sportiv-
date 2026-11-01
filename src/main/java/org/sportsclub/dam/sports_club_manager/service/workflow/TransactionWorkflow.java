package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionCategory;
import org.sportsclub.dam.sports_club_manager.domain.enums.TransactionType;
import org.sportsclub.dam.sports_club_manager.domain.model.Season;
import org.sportsclub.dam.sports_club_manager.domain.model.Transaction;
import org.sportsclub.dam.sports_club_manager.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
public interface TransactionWorkflow {
 Transaction record(Season season, TransactionType type, TransactionCategory category, BigDecimal amount, LocalDate date, String description, User actor);
 Transaction reverse(Season season, UUID transactionId, User actor);
}
