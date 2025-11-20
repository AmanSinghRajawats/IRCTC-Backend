package com.irctc.TrainTicketBooking.dtos;

import com.irctc.TrainTicketBooking.enums.CoachType;
import lombok.Data;

@Data
public class CoachResponseDTO {
    private Long coachId;
    private String coachNumber;
    private CoachType coachType;
    private Integer totalSeats;
    private Integer availableSeats;
}
