package org.sportsclub.dam.sports_club_manager.service.computation;
import org.sportsclub.dam.sports_club_manager.domain.model.Contract;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
@Service public class ContractCostComputationService implements ContractCostComputation {
 public BigDecimal annualSalary(Contract contract) { return contract.getMonthlySalary().multiply(BigDecimal.valueOf(12)); }
 public BigDecimal activationCost(Contract contract) { return annualSalary(contract).add(contract.getTransferFee()); }
}
