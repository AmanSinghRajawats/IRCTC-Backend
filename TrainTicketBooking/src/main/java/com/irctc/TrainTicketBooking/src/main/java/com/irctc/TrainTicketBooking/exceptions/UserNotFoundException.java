package com.irctc.TrainTicketBooking.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }
    UserNotFoundException(){}
}
