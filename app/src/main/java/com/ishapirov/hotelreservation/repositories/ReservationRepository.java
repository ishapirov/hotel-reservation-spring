package com.ishapirov.hotelreservation.repositories;

import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation,Integer> {
    Optional<Reservation>findByReservationNumber(Integer reservationNumber);
    List<Reservation> findByRoom(Room room);
    List<Reservation> findByRoomAndCancelled(Room room,boolean cancelled);
    Long deleteByReservationNumber(Integer reservationNumber);
}
