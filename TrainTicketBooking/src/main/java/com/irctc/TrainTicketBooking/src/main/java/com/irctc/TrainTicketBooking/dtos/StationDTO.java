package com.irctc.TrainTicketBooking.dtos;

import lombok.Data;

@Data
public class StationDTO {
    private String stationCode;
    private String name;
    private String city;
    private String state;
}
