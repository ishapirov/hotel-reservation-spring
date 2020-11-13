package com.ishapirov.hotelapi.services.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelReservation {
    @NotNull
    @Positive
    private Integer reservationNumber;
}
