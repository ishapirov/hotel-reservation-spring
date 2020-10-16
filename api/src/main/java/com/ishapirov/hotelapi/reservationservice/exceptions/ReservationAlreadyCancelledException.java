package com.ishapirov.hotelapi.reservationservice.exceptions;

public class ReservationAlreadyCancelledException extends RuntimeException {
    public ReservationAlreadyCancelledException(){
        super();
    }
    public ReservationAlreadyCancelledException(String message){
        super(message);
    }
}
