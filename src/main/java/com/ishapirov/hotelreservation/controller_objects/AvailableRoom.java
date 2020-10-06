package com.ishapirov.hotelreservation.controller_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoom {
    private String roomtypeName;
    //Dates must be in yyyy-mm-dd format
    private String checkInDate;
    private String checkOutDate;
}
