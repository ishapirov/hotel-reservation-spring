package com.ishapirov.hotelapi.services.reservation.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReservation {

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
