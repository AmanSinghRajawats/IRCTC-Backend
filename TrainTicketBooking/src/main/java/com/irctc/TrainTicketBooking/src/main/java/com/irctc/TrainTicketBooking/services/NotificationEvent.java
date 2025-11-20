package com.irctc.TrainTicketBooking.services;

import com.irctc.TrainTicketBooking.dtos.BookingResponse;
import com.irctc.TrainTicketBooking.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;

@Getter
@Setter
public class NotificationEvent {

    private String email;
    private String subject;
    private String message;
    private String pnr;
    private Long trainNumber;

//    private BookingResponse bookingResponse;

    private BookingStatus bookingStatus; // BOOKING_CONFIRMED, etc.
}
