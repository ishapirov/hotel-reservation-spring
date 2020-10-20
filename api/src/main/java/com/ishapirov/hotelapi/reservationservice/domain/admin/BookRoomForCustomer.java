package com.ishapirov.hotelapi.reservationservice.domain.admin;

import java.util.Date;

import com.ishapirov.hotelapi.reservationservice.domain.BookRoom;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class BookRoomForCustomer {
    @NotNull
    @Size(min=5,max=32)
	private String username;

    @NotNull
    @Min(1985)
    @Max(2084)
    private Integer roomNumber;

    @NotNull
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent
    private Date checkInDate;

    @NotNull
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent
    private Date checkOutDate;

    public BookRoomForCustomer(String username, BookRoom bookRoom) {
        this.username = username;
        this.roomNumber = bookRoom.getRoomNumber();
        this.checkInDate = bookRoom.getCheckInDate();
        this.checkOutDate = bookRoom.getCheckOutDate(); 
	}
}
