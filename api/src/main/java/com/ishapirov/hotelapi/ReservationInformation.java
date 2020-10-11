package com.ishapirov.hotelapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationInformation {

    private Integer reservationNumber;
    private String customerFirstName;
    private String customerLastName;
    private RoomInformation roomInformation;

}
