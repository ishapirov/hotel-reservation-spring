package com.ishapirov.hotelapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInformation {

    private Integer reservationNumber;
    private CustomerInformation customerInformation;
    private RoomInformation roomInformation;
    private Date checkInDate;
    private Date checkOutDate;

}
