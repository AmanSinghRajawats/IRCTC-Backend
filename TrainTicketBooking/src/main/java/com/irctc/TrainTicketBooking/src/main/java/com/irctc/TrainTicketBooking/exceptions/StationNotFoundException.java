package com.irctc.TrainTicketBooking.exceptions;

public class StationNotFoundException extends RuntimeException{

    public StationNotFoundException(String message){
        super(message);
    }

    public StationNotFoundException() {
    }
}
