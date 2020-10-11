package com.ishapirov.hotelreservation.util;

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

    public List<Room> returnRooms(List<Room> rooms, Date checkinDate, Date checkoutDate) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms){
            List<Reservation> reservations = reservationRepository.findByRoom(room);
            if(isDateAvailable(reservations, checkinDate, checkoutDate))
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

}
