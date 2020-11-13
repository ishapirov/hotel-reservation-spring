package com.ishapirov.hotelapi.services.room.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Integer roomNumber;
    private RoomTypeInformation roomType;
    private Double roomPrice;

}
