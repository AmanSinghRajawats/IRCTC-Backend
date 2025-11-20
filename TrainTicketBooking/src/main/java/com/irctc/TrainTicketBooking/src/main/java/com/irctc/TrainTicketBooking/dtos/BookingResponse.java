package com.irctc.TrainTicketBooking.dtos;

import com.irctc.TrainTicketBooking.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private String message;
    private String pnr;
    private Double totalFare;
    private String trainName;
    private String from;
    private String to;
    private BookingStatus bookingStatus;
    private LocalDate journeyDate;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
}
