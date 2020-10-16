package com.ishapirov.hotelapi.roomservice.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(){
        super();
    }
    public RoomNotFoundException(String message){
        super(message);
    }
}
