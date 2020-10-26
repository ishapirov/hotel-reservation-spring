package com.ishapirov.hotelapi.reservationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationBasicInformation {
    private Integer reservationNumber;
    private String username;
    private Integer roomNumber;
    private Date checkInDate;
    private Date checkOutDate;
}
