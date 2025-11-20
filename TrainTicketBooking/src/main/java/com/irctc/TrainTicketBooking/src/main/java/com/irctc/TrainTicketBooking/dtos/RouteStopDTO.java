package com.irctc.TrainTicketBooking.dtos;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class RouteStopDTO {
    private int sequenceNumber;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private int haltDuration;  // in minutes
    private int distanceFromOrigin;  // in km
    private StationDTO stationDTO;
}
