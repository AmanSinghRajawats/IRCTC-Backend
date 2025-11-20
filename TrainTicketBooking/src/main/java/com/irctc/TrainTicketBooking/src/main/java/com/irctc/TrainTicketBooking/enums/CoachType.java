package com.irctc.TrainTicketBooking.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CoachType {
    SLEEPER("S"),          // Typically 72 seats in Sleeper
    AC_3_TIER("B"),        // Typically 64 seats in AC 3-Tier
    AC_2_TIER("A"),        // Typically 48 seats in AC 2-Tier
    FIRST_CLASS("F"),      // Typically 24 seats in First Class
    GENERAL("UR"),        // Typically ~100 seats in General (Unreserved)
    LADIES("L");           // Typically ~50 seats in Ladies Coach

    private final String prefix;
//    private final int defaultSeats;  // Default seat capacity per coach

    CoachType(String prefix) {
        this.prefix = prefix;
    }

    @JsonCreator
    public static CoachType fromString(String value) {
        for (CoachType type : CoachType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Invalid coachType: " + value +
                        ". Allowed values are: " + Arrays.toString(CoachType.values())
        );
    }
}
