package com.ishapirov.hotelapi.userservice.exceptions;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(){
        super();
    }
    public CustomerNotFoundException(String message){
        super(message);
    }
}
