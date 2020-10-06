package com.ishapirov.hotelreservation.controller_objects;

import lombok.Data;

@Data
public class BookRoomForCustomer {
	private String username;
    private Integer roomNumber;
    private String checkInDate;
    private String checkOutDate;

    public BookRoomForCustomer(String username, BookRoom bookRoom) {
        this.username = username;
        this.roomNumber = bookRoom.getRoomNumber();
        this.checkInDate = bookRoom.getCheckInDate();
        this.checkOutDate = bookRoom.getCheckOutDate(); 
	}
}
