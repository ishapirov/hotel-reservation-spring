package com.ishapirov.hotelapi.reservationservice.exceptions;

public class ReservationCreationError extends RuntimeException {
    public ReservationCreationError(){
        super();
    }
    public ReservationCreationError(String message){
        super(message);
    }
}
