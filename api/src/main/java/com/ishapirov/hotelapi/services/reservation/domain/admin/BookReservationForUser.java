package com.ishapirov.hotelapi.services.reservation.domain.admin;

import java.util.Date;

import com.ishapirov.hotelapi.services.reservation.domain.BookReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReservationForUser {
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

    public BookReservationForUser(String username, BookReservation bookReservation) {
        this.username = username;
        this.roomNumber = bookReservation.getRoomNumber();
        this.checkInDate = bookReservation.getCheckInDate();
        this.checkOutDate = bookReservation.getCheckOutDate();
	}
}
