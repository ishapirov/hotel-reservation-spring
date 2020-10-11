package com.ishapirov.hotelapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomInformation {

    private Integer roomNumber;
    private String roomType;
    private Double roomPrice;

}
