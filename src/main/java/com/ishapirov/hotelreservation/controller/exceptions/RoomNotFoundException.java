package com.ishapirov.hotelreservation.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "A room with the given room number was not found")
public class RoomNotFoundException extends RuntimeException {
    
}
