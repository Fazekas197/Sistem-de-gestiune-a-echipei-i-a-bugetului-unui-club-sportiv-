package org.sportsclub.dam.sports_club_manager.service.computation;
import org.sportsclub.dam.sports_club_manager.domain.model.Contract;
import java.math.BigDecimal;
public interface ContractCostComputation { BigDecimal annualSalary(Contract contract); BigDecimal activationCost(Contract contract); }
