package com.ishapirov.hotelapi.services.room.paramvalidation;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;

@Data
public class OneRoomCriteria {


    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent
    private Date checkInDate;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @FutureOrPresent
    private Date checkOutDate;

}
