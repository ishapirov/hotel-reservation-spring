package com.ishapirov.hotelapi.reservationservice.exceptions;


public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(){
        super();
    }
    public ReservationNotFoundException(String message){
        super(message);
    }
}
