package com.ishapirov.hotelapi;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BookRoom {
    private Integer roomNumber;
    private Date checkInDate;
    private Date checkOutDate;
}
