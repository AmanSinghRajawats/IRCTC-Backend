package com.irctc.TrainTicketBooking.exceptions;

public class TicketCancellationException extends RuntimeException{
    public TicketCancellationException(String message){
        super(message);
    }

    public TicketCancellationException() {
    }
}
