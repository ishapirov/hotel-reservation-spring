package com.ishapirov.hotelapi.services.reservation.exceptions;


public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(){
        super();
    }
    public ReservationNotFoundException(String message){
        super(message);
    }
}
