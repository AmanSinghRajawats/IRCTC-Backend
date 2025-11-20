package com.irctc.TrainTicketBooking.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class BookingRequest {

    private Long trainNumber;
    private String userEmail;
    private Station from;
    private Station to;
    private LocalDateTime date;
    private List<Passenger> passengers;

}
