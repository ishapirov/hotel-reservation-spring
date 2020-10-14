package com.ishapirov.hotelapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The check in date provided is from the past")
public class DatesInvalidException extends  RuntimeException {
    public DatesInvalidException(){
        super();
    }
    public DatesInvalidException(String message){
        super(message);
    }
}
