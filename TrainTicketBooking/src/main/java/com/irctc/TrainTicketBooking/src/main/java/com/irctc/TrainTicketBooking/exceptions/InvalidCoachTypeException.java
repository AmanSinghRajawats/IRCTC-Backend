package com.irctc.TrainTicketBooking.exceptions;

public class InvalidCoachTypeException extends RuntimeException{
    public InvalidCoachTypeException(String message){
        super(message);
    }

    public InvalidCoachTypeException() {
    }
}
