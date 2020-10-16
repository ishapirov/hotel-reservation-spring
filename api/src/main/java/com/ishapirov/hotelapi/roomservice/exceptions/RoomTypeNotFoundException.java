package com.ishapirov.hotelapi.roomservice.exceptions;

public class RoomTypeNotFoundException extends RuntimeException {
    public RoomTypeNotFoundException(){
        super();
    }
    public RoomTypeNotFoundException(String message){
        super(message);
    }
}
