package com.ishapirov.hotelapi.reservationservice.exceptions;

public class ReservationUsernamesDoNotMatchException extends RuntimeException {
    public ReservationUsernamesDoNotMatchException(){
        super();
    }
    public ReservationUsernamesDoNotMatchException(String message){
        super(message);
    }
}
