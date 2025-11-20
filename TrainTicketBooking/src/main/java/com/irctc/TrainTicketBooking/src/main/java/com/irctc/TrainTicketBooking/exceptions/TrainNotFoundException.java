package com.irctc.TrainTicketBooking.exceptions;

public class TrainNotFoundException extends RuntimeException{
    public TrainNotFoundException(String message){
        super(message);
    }

    public TrainNotFoundException() {
    }
}
