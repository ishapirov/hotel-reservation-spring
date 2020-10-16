package com.ishapirov.hotelapi.roomservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdate {
    private String roomType;
    private Double roomPrice;
}
