package com.ishapirov.hotelapi.userservice.paramvalidation;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Data
public class UsersCriteria {
    @PositiveOrZero
    private Integer pageNumber = 0;

    @PositiveOrZero
    @Max(100)
    private Integer size = 25;
}
