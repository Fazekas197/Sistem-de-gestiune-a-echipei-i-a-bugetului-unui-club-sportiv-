package org.sportsclub.dam.sports_club_manager.service.workflow;
import org.sportsclub.dam.sports_club_manager.domain.enums.ContractType;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
public interface ContractWorkflowService {
 Contract propose(UUID playerId, ContractType type, String originClub, BigDecimal monthlySalary, BigDecimal transferFee, LocalDate start, LocalDate end, User actor);
 Contract activate(UUID playerId, UUID contractId, Season season, User actor);
 Contract expire(UUID playerId, UUID contractId, User actor);
 Contract terminate(UUID playerId, UUID contractId, User actor);
 List<Contract> findContracts(UUID playerId, User actor);
}
