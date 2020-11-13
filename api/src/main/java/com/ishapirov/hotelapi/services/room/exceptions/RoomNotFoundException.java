package com.ishapirov.hotelapi.services.room.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(){
        super();
    }
    public RoomNotFoundException(String message){
        super(message);
    }
}
