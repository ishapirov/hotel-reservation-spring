package com.ishapirov.hotelreservation.repositories;

import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelapi.Reservation;
import com.ishapirov.hotelapi.Room;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation,Integer> {
    Optional<Reservation>findByReservationNumber(Integer reservationNumber);
    List<Reservation> findByRoom(Room room);
    Long deleteByReservationNumber(Integer reservationNumber);
}
