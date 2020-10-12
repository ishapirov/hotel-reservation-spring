package com.ishapirov.hotelapi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRoom {
    private Integer roomNumber;
    private Date checkInDate;
    private Date checkOutDate;
}
