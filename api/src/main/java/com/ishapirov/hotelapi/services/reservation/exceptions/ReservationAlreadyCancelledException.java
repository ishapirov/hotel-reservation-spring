package com.ishapirov.hotelapi.services.reservation.exceptions;

public class ReservationAlreadyCancelledException extends RuntimeException {
    public ReservationAlreadyCancelledException(){
        super();
    }
    public ReservationAlreadyCancelledException(String message){
        super(message);
    }
}
