package com.ishapirov.hotelapi.reservationservice.domain.admin;

import java.util.Date;

import com.ishapirov.hotelapi.reservationservice.domain.BookRoom;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
