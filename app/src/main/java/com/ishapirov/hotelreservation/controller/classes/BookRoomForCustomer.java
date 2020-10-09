package com.ishapirov.hotelreservation.controller.classes;

import java.util.Date;

import lombok.Data;

@Data
public class BookRoomForCustomer {
	private String username;
    private Integer roomNumber;
    private Date checkInDate;
    private Date checkOutDate;

    public BookRoomForCustomer(String username, BookRoom bookRoom) {
        this.username = username;
        this.roomNumber = bookRoom.getRoomNumber();
        this.checkInDate = bookRoom.getCheckInDate();
        this.checkOutDate = bookRoom.getCheckOutDate(); 
	}
}
