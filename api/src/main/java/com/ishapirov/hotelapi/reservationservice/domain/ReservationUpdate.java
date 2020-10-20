package com.ishapirov.hotelapi.reservationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdate {

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
}
