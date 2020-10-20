package com.ishapirov.hotelapi.roomservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdate {
    @Size(max=32)
    private String roomType;

    @Max(10000000)
    @Positive
    private Double roomPrice;
}
