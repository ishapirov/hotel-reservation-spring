package com.ishapirov.hotelapi.services.room.paramvalidation;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;


@Data
public class RoomsCriteria {
    private String roomType;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent
    private Date checkInDate;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent
    private Date checkOutDate;

    @PositiveOrZero
    private Integer pageNumber = 0;

    @PositiveOrZero
    @Max(100)
    private Integer size = 25;
}
