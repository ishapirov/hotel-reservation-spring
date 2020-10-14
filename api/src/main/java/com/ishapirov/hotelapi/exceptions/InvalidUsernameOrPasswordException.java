package com.ishapirov.hotelapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "The username or password was incorrect.")
public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(){
        super();
    }
    public InvalidUsernameOrPasswordException(String message){
        super(message);
    }
}
