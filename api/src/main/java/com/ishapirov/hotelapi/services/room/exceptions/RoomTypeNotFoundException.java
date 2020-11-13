package com.ishapirov.hotelapi.services.room.exceptions;

public class RoomTypeNotFoundException extends RuntimeException {
    public RoomTypeNotFoundException(){
        super();
    }
    public RoomTypeNotFoundException(String message){
        super(message);
    }
}
