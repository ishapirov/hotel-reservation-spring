package com.ishapirov.hotelapi.services.reservation.domain;

import com.ishapirov.hotelapi.services.room.domain.RoomResponse;
import com.ishapirov.hotelapi.services.user.domain.UserInformation;
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
