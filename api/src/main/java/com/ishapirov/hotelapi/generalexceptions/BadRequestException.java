package com.ishapirov.hotelapi.generalexceptions;

public class BadRequestException extends  RuntimeException {
    public BadRequestException(String message){
        super(message);
    }
}
