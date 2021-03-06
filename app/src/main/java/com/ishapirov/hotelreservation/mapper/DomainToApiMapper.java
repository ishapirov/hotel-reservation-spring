package com.ishapirov.hotelreservation.mapper;

import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.reservation.domain.ReservationBasicInformation;
import com.ishapirov.hotelapi.services.reservation.domain.ReservationResponse;
import com.ishapirov.hotelapi.services.room.domain.RoomBasicInformation;
import com.ishapirov.hotelapi.services.room.domain.RoomResponse;
import com.ishapirov.hotelapi.services.user.domain.UserInformation;
import com.ishapirov.hotelapi.services.reservation.domain.ReservationInformation;
import com.ishapirov.hotelapi.services.room.domain.RoomInformation;
import com.ishapirov.hotelapi.services.room.domain.RoomTypeInformation;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


    public HotelPage<RoomBasicInformation> getBasicRoomsInformation(Page<Room> rooms, Pageable pageable){
        return new HotelPage<RoomBasicInformation>(rooms.getContent().stream()
                .map(this::getBasicRoomInformation)
                .collect(Collectors.toList()),rooms.getTotalPages(),rooms.hasNext());
    }

    public HotelPage<ReservationBasicInformation> getReservationInformationPage(Page<Reservation> reservations, Pageable pageable){
        return new HotelPage<ReservationBasicInformation>(reservations.getContent().stream()
                .map(this::getReservationBasicInformation)
                .collect(Collectors.toList()),reservations.getTotalPages(),reservations.hasNext());
    }

    public HotelPage<UserInformation> getUserInformationPage(Page<User> users, Pageable pageable){
        return new HotelPage<UserInformation>(users.getContent().stream()
                .map(this::getUserInformation)
                .collect(Collectors.toList()),users.getTotalPages(),users.hasNext());
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
        reservationInformation.setCancelled(reservation.isCancelled());
        return reservationInformation;
    }

    public ReservationBasicInformation getReservationBasicInformation(Reservation reservation){
        ReservationBasicInformation reservationBasicInformation = new ReservationBasicInformation();
        reservationBasicInformation.setReservationNumber(reservation.getReservationNumber());
        reservationBasicInformation.setRoomNumber(reservation.getRoom().getRoomNumber());
        reservationBasicInformation.setUsername(reservation.getUser().getUserSecurity().getUsername());
        reservationBasicInformation.setCheckInDate(reservation.getCheckInDate());
        reservationBasicInformation.setCheckOutDate(reservation.getCheckOutDate());
        return reservationBasicInformation;
    }

    public ReservationResponse getReservationResponse(Reservation reservation){
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setReservationNumber(reservation.getReservationNumber());
        reservationResponse.setRoomInformation(getRoomResponse(reservation.getRoom()));
        reservationResponse.setUserInformation(getUserInformation(reservation.getUser()));
        reservationResponse.setCheckInDate(reservation.getCheckInDate());
        reservationResponse.setCheckOutDate(reservation.getCheckOutDate());
        return reservationResponse;
    }

    public UserInformation getUserInformation(User user){
        UserInformation userInformation = new UserInformation();
        userInformation.setUserID(user.getUserID());
        userInformation.setUsername(user.getUserSecurity().getUsername());
        userInformation.setEmail(user.getEmail());
        userInformation.setFirstName(user.getFirstName());
        userInformation.setLastName(user.getLastName());
        return userInformation;
    }

}
