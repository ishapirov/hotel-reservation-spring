package com.ishapirov.hotelapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "A customer with the given id was not found")
public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(){
        super();
    }
    public CustomerNotFoundException(String message){
        super(message);
    }
}
