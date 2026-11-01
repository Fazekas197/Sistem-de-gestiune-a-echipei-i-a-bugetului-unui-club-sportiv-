package org.sportsclub.dam.sports_club_manager.domain.factory;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
@Component
public class PlayerFactory {
    public Player create(String name, LocalDate birthDate, String position, Integer jerseyNumber) {
        return new Player(name, birthDate, position, jerseyNumber);
    }
}
