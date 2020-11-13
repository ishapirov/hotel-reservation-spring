package com.ishapirov.hotelapi.services.reservation.paramvalidation;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class ReservationsCriteria {
    @PositiveOrZero
    private Integer pageNumber = 0;

    @PositiveOrZero
    @Max(100)
    private Integer size = 25;

    @Size(min=5,max=32)
    private String username;
}
