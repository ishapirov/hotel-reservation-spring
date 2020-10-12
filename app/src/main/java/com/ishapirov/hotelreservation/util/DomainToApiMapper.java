package com.ishapirov.hotelreservation.util;

import com.ishapirov.hotelapi.CustomerInformation;
import com.ishapirov.hotelapi.ReservationInformation;
import com.ishapirov.hotelapi.RoomInformation;
import com.ishapirov.hotelapi.RoomTypeInformation;
import com.ishapirov.hotelreservation.hotelclasses.Customer;
import com.ishapirov.hotelreservation.hotelclasses.Reservation;
import com.ishapirov.hotelreservation.hotelclasses.Room;
import com.ishapirov.hotelreservation.hotelclasses.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainToApiMapper {

    public RoomInformation getRoomInformation(Room room){
        RoomInformation roomInformation = new RoomInformation();
        roomInformation.setRoomNumber(room.getRoomNumber());
        roomInformation.setRoomType(getRoomTypeInformation(room.getRoomType()));
        roomInformation.setRoomPrice(room.getRoomPrice());
        return roomInformation;
    }

    public RoomTypeInformation getRoomTypeInformation(RoomType roomType){
        RoomTypeInformation roomTypeInformation = new RoomTypeInformation();
        roomTypeInformation.setName(roomType.getName());
        return roomTypeInformation;
    }

    public List<RoomInformation> getRoomsInformation(List<Room> rooms){
        return rooms.stream()
                .map(this::getRoomInformation)
                .collect(Collectors.toList());
    }

    public ReservationInformation getReservationInformation(Reservation reservation){
        ReservationInformation reservationInformation = new ReservationInformation();
        reservationInformation.setReservationNumber(reservation.getReservationNumber());
        reservationInformation.setRoomInformation(getRoomInformation(reservation.getRoom()));
        reservationInformation.setCustomerInformation(getCustomerInformation(reservation.getCustomer()));
        reservationInformation.setCheckInDate(reservation.getCheckInDate());
        reservationInformation.setCheckOutDate(reservation.getCheckOutDate());
        return reservationInformation;
    }

    public CustomerInformation getCustomerInformation(Customer customer){
        CustomerInformation customerInformation = new CustomerInformation();
        customerInformation.setUsername(customer.getUsername());
        customerInformation.setEmail(customer.getEmail());
        customerInformation.setFirstName(customer.getFirstName());
        customerInformation.setLastName(customer.getLastName());
        return customerInformation;
    }

}
