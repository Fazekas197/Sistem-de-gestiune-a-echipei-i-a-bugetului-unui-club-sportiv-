package org.sportsclub.dam.sports_club_manager.domain.model;

import jakarta.validation.constraints.Positive;

 
public record Season(@Positive int startYear, @Positive int endYear) {
    public Season {
        if (startYear <= 0 || endYear != startYear + 1) {
            throw new IllegalArgumentException("A season must contain two consecutive positive years.");
        }
    }
    @Override public String toString() { return startYear + "-" + endYear; }
}
