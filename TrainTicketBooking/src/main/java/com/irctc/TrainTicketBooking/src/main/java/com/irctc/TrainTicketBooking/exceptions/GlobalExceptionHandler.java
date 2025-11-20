package com.irctc.TrainTicketBooking.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(UserNotFoundException userNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                userNotFoundException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<ErrorResponse> trainNotFoundExceptionHandler(TrainNotFoundException trainNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                trainNotFoundException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeatsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> seatsNotAvailableExceptionHandler(SeatsNotAvailableException seatsNotAvailableException){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                seatsNotAvailableException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketCancellationException.class)
    public ResponseEntity<ErrorResponse> ticketCancellationExceptionHandler(TicketCancellationException ticketCancellationException){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.ALREADY_REPORTED.value(), // already cancelled
                ticketCancellationException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> stationNotFoundExceptionHandler(StationNotFoundException stationNotFoundException){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), // already cancelled
                stationNotFoundException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> bookingExceptionHandler(BookingException bookingException){
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                bookingException.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleJsonMappingException(JsonMappingException ex) {

        String message = ex.getOriginalMessage(); // Jackson error message

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", message,
                "path", "/api/v1/users/book"
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", ex.getMessage()
        ));
    }

}
