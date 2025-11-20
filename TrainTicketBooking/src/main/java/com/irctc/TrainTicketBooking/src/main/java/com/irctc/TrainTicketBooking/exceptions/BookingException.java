package com.irctc.TrainTicketBooking.exceptions;

import com.irctc.TrainTicketBooking.entities.BookingRequest;

public class BookingException extends RuntimeException{
    BookingException(String message){
        super(message);
    }

    public BookingException() {
    }
}
