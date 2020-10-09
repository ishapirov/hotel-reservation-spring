package com.ishapirov.hotelreservation.controller.classes;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoom {
    private String roomTypeName;
    //Dates must be in yyyy-mm-dd format
    private Date checkInDate;
    private Date checkOutDate;
}
