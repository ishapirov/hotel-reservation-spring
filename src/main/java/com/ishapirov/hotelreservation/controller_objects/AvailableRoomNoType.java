package com.ishapirov.hotelreservation.controller_objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomNoType {
    private String checkInDate;
    private String checkOutDate;

    public AvailableRoomNoType(AvailableRoom availableRoom){
        this.checkInDate = availableRoom.getCheckInDate();
        this.checkOutDate = availableRoom.getCheckOutDate();
    }
}
