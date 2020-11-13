package com.ishapirov.hotelapi.services.reservation.exceptions;

public class ReservationOverlapException extends RuntimeException{
    public ReservationOverlapException(){
        super();
    }
    public ReservationOverlapException(String message){
        super(message);
    }
}
