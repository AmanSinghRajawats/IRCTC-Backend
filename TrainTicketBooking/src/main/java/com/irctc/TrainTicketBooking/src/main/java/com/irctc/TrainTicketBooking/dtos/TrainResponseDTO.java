package com.irctc.TrainTicketBooking.dtos;

import com.irctc.TrainTicketBooking.enums.RunningDays;
import com.irctc.TrainTicketBooking.enums.TrainType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TrainResponseDTO {
    private Long trainId;
    private Long trainNumber;
    private String trainName;
    private TrainType trainType;
    private Set<RunningDays> runningDays;
    private List<CoachResponseDTO> coaches;
}