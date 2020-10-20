package com.ishapirov.hotelapi.reservationservice.domain.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelReservationForCustomer {
    @NotNull
    @Positive
    private Integer reservationNumber;

    @NotNull
    @Size(min=5,max=32)
    private String username;
}
