package com.irctc.TrainTicketBooking.exceptions;

public class SeatsNotAvailableException extends RuntimeException{

    SeatsNotAvailableException(String message){
        super(message);
    }
    public SeatsNotAvailableException() {
    }
}
