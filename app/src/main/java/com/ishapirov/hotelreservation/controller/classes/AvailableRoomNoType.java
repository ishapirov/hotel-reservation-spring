package com.ishapirov.hotelreservation.controller.classes;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomNoType {
    private Date checkInDate;
    private Date checkOutDate;

    public AvailableRoomNoType(AvailableRoom availableRoom){
        this.checkInDate = availableRoom.getCheckInDate();
        this.checkOutDate = availableRoom.getCheckOutDate();
    }
}
