package org.sportsclub.dam.sports_club_manager.service.computation;
import org.sportsclub.dam.sports_club_manager.domain.enums.PlayerStatus;
import org.sportsclub.dam.sports_club_manager.domain.model.Player;
public interface PlayerStatusComputation { PlayerStatus compute(Player player); }
