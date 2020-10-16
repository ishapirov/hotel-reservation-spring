package com.ishapirov.hotelapi.reservationservice.domain;

import com.ishapirov.hotelapi.roomservice.domain.RoomResponse;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private Integer reservationNumber;
    private UserInformation userInformation;
    private RoomResponse roomInformation;
    private Date checkInDate;
    private Date checkOutDate;

}
