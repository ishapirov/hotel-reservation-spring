package com.ishapirov.hotelapi.generalexceptions;

public class DatesInvalidException extends  RuntimeException {
    public DatesInvalidException(){
        super();
    }
    public DatesInvalidException(String message){
        super(message);
    }
}
