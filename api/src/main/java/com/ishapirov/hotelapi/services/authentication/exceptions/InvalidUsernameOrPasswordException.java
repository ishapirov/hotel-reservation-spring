package com.ishapirov.hotelapi.services.authentication.exceptions;

public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(){
        super();
    }
    public InvalidUsernameOrPasswordException(String message){
        super(message);
    }
}
