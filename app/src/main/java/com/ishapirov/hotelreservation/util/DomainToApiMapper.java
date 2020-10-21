package com.ishapirov.hotelreservation.util;

import com.ishapirov.hotelapi.reservationservice.domain.ReservationResponse;
import com.ishapirov.hotelapi.roomservice.domain.RoomBasicInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomResponse;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomTypeInformation;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainToApiMapper {

    public RoomBasicInformation getBasicRoomInformation(Room room){
        RoomBasicInformation roomBasicInformation = new RoomBasicInformation();
        roomBasicInformation.setRoomNumber(room.getRoomNumber());
        roomBasicInformation.setRoomType(room.getRoomType().getName());
        roomBasicInformation.setRoomPrice(room.getRoomPrice());
        return roomBasicInformation;
    }

    public RoomInformation getRoomInformation(Room room){
        RoomInformation roomInformation = new RoomInformation();
        roomInformation.setRoomNumber(room.getRoomNumber());
        roomInformation.setRoomType(room.getRoomType().getName());
        roomInformation.setRoomPrice(room.getRoomPrice());
        return roomInformation;
    }

    public RoomResponse getRoomResponse(Room room){
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setRoomNumber(room.getRoomNumber());
        roomResponse.setRoomType(getRoomTypeInformation(room.getRoomType()));
        roomResponse.setRoomPrice(room.getRoomPrice());
        return roomResponse;
    }


    public Page<RoomBasicInformation> getBasicRoomsInformation(Page<Room> rooms, Pageable pageable){
        return new PageImpl<>(rooms.getContent().stream()
                .map(this::getBasicRoomInformation)
                .collect(Collectors.toList()),pageable,rooms.getContent().size());
    }

    public Page<ReservationInformation> getReservationInformationPage(Page<Reservation> reservations, Pageable pageable){
        return new PageImpl<>(reservations.getContent().stream()
                .map(this::getReservationInformation)
                .collect(Collectors.toList()),pageable,reservations.getContent().size());
    }

    public RoomTypeInformation getRoomTypeInformation(RoomType roomType){
        RoomTypeInformation roomTypeInformation = new RoomTypeInformation();
        roomTypeInformation.setName(roomType.getName());
        return roomTypeInformation;
    }

    public ReservationInformation getReservationInformation(Reservation reservation){
        ReservationInformation reservationInformation = new ReservationInformation();
        reservationInformation.setReservationNumber(reservation.getReservationNumber());
        reservationInformation.setRoomNumber(reservation.getRoom().getRoomNumber());
        reservationInformation.setUsername(reservation.getUser().getUserSecurity().getUsername());
        reservationInformation.setCheckInDate(reservation.getCheckInDate());
        reservationInformation.setCheckOutDate(reservation.getCheckOutDate());
        return reservationInformation;
    }

    public ReservationResponse getReservationResponse(Reservation reservation){
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setReservationNumber(reservation.getReservationNumber());
        reservationResponse.setRoomInformation(getRoomResponse(reservation.getRoom()));
        reservationResponse.setUserInformation(getCustomerInformation(reservation.getUser()));
        reservationResponse.setCheckInDate(reservation.getCheckInDate());
        reservationResponse.setCheckOutDate(reservation.getCheckOutDate());
        return reservationResponse;
    }

    public UserInformation getCustomerInformation(User user){
        UserInformation userInformation = new UserInformation();
        userInformation.setUsername(user.getUserSecurity().getUsername());
        userInformation.setEmail(user.getEmail());
        userInformation.setFirstName(user.getFirstName());
        userInformation.setLastName(user.getLastName());
        return userInformation;
    }

}
