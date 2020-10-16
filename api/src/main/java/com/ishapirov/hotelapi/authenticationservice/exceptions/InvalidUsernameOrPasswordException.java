package com.ishapirov.hotelapi.authenticationservice.exceptions;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(){
        super();
    }
    public InvalidUsernameOrPasswordException(String message){
        super(message);
    }
}
