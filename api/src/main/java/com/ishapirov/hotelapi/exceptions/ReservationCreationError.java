package com.ishapirov.hotelapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error while creating reservation")
public class ReservationCreationError extends RuntimeException {
    public ReservationCreationError(){
        super();
    }
    public ReservationCreationError(String message){
        super(message);
    }
}
