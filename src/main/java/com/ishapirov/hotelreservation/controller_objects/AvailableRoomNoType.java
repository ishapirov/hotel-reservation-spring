package com.ishapirov.hotelreservation.controller_objects;

import java.util.Date;

import lombok.Data;

@Data
public class AvailableRoomNoType {
    private Date checkinDate;
    private Date checkoutDate;
}
