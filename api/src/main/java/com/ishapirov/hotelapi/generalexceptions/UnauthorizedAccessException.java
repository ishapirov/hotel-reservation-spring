package com.ishapirov.hotelapi.generalexceptions;

public class UnauthorizedAccessException extends  RuntimeException{
    public UnauthorizedAccessException(){
        super();
    }
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
