package com.ishapirov.hotelapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The username on the reservation does not match the username of the logged in user.")
public class ReservationUsernamesDoNotMatchException extends RuntimeException {
    
}
