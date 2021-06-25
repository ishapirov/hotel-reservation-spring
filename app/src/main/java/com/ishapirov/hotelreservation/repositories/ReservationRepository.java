package com.ishapirov.hotelreservation.repositories;

import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends PagingAndSortingRepository<Reservation,Integer> {
    Optional<Reservation> findByReservationNumber(Integer reservationNumber);
    List<Reservation> findByRoom(Room room);
    List<Reservation> findByRoomAndCancelled(Room room,boolean cancelled);
    Page<Reservation> findAllByUser_Username(String username, Pageable page);
}
