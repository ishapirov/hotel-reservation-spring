package com.ishapirov.hotelapi.services.reservation.exceptions;

public class ReservationDeletionError extends RuntimeException {
    public ReservationDeletionError(){
        super();
    }
    public ReservationDeletionError(String message){
        super(message);
    }
}
