package com.ishapirov.hotelapi.reservationservice.exceptions;

public class ReservationDeletionError extends RuntimeException {
    public ReservationDeletionError(){
        super();
    }
    public ReservationDeletionError(String message){
        super(message);
    }
}
