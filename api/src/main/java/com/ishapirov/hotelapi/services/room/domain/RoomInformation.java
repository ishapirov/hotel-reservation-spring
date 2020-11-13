package com.ishapirov.hotelapi.services.room.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInformation {

    private Integer roomNumber;
    private String roomType;
    private Double roomPrice;

}
