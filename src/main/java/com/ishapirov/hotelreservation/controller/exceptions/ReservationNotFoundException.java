package com.ishapirov.hotelreservation.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "A reservation with the given reservation number was not found")
public class ReservationNotFoundException extends RuntimeException{
    
}
