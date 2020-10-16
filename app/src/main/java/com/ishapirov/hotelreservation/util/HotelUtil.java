package com.ishapirov.hotelreservation.util;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotelUtil {
    @Autowired
    ReservationRepository reservationRepository;

    public List<Room> filterRooms(List<Room> rooms, Date checkInDate, Date checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms){
            List<Reservation> reservations = reservationRepository.findByRoomAndCancelled(room,false);
            if(isDateAvailable(reservations, checkInDate, checkOutDate))
                availableRooms.add(room);
        }
        return availableRooms;
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
