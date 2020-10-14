package com.ishapirov.hotelapi.domainapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInformation {

    private Integer roomNumber;
    private RoomTypeInformation roomType;
    private Double roomPrice;

}
