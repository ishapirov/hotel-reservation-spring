package com.ishapirov.hotelapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "A room type with the given room type name was not found")
public class RoomTypeNotFoundException extends RuntimeException {
    public RoomTypeNotFoundException(){
        super();
    }
    public RoomTypeNotFoundException(String message){
        super(message);
    }
}
