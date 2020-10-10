package com.ishapirov.hotelapi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelReservation {
    private Integer reservationNumber;
}
