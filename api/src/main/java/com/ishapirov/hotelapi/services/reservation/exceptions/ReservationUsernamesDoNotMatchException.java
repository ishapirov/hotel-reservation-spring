package com.ishapirov.hotelapi.services.reservation.exceptions;

public class ReservationUsernamesDoNotMatchException extends RuntimeException {
    public ReservationUsernamesDoNotMatchException(){
        super();
    }
    public ReservationUsernamesDoNotMatchException(String message){
        super(message);
    }
}
