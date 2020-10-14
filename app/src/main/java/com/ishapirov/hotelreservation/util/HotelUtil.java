package com.ishapirov.hotelreservation.util;

import com.ishapirov.hotelapi.exceptions.DatesInvalidException;
import com.ishapirov.hotelreservation.hotelclasses.Reservation;
import com.ishapirov.hotelreservation.hotelclasses.Room;
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

    public List<Room> returnRooms(List<Room> rooms, Date checkInDate, Date checkOutDate) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms){
            List<Reservation> reservations = reservationRepository.findByRoom(room);
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
        if(checkInDate.compareTo(new Date())<0)
            throw new DatesInvalidException("The check in date has been set for a date in the past");
        if(checkOutDate.compareTo(checkInDate)<0)
            throw new DatesInvalidException("The check out date is before the check in date");
    }

}
