package com.ishapirov.hotelreservation.util;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelUtil {
    @Autowired
    ReservationRepository reservationRepository;

    public Page<Room> filterRooms(Page<Room> rooms, Date checkInDate, Date checkOutDate, Pageable pageable) {
        List<Room> roomList = rooms.getContent();
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : roomList){
            List<Reservation> reservations = reservationRepository.findByRoomAndCancelled(room,false);
            if(isDateAvailable(reservations, checkInDate, checkOutDate))
                availableRooms.add(room);
        }
        return new PageImpl<>(availableRooms,pageable,rooms.getContent().size());
    }

    public boolean isDateAvailable(List<Reservation> reservations,Date checkin, Date checkout){
        for(Reservation reservation: reservations){
            if(!reservation.isAvailable(checkin, checkout)){
                return false;
            }
        }
        return true;
    }

    public void validateDates(Date checkInDate,Date checkOutDate){
        if((checkInDate == null && checkOutDate != null) ||(checkInDate != null && checkOutDate == null))
            throw new BadRequestException("Only a check in date or check out date have been provided, but not both.");
        if(checkInDate.compareTo(new Date())<0)
            throw new DatesInvalidException("The check in date has been set for a date in the past");
        if(checkOutDate.compareTo(checkInDate)<0)
            throw new DatesInvalidException("The check out date is before the check in date");
    }

}
