package com.ishapirov.hotelapi.reservationservice.exceptions;

public class ReservationOverlapException extends RuntimeException{
    public ReservationOverlapException(){
        super();
    }
    public ReservationOverlapException(String message){
        super(message);
    }
}
