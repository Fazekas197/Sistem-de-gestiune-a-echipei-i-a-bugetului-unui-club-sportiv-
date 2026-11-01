package org.sportsclub.dam.sports_club_manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sportsclub.dam.sports_club_manager.service.workflow.ContractWorkflowService;
import org.sportsclub.dam.sports_club_manager.service.workflow.PlayerWorkflowService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SportsClubManagerApplicationTests {

    @Autowired private PlayerWorkflowService playerWorkflowService;
    @Autowired private ContractWorkflowService contractWorkflowService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        assertNotNull(playerWorkflowService);
        assertNotNull(contractWorkflowService);
        assertNotNull(passwordEncoder);
    }
}
