package com.ishapirov.hotelreservation.repositories;

import java.util.List;

import com.ishapirov.hotelreservation.hotel_objects.DatesBookedInterval;
import com.ishapirov.hotelreservation.hotel_objects.Reservation;

import org.springframework.data.repository.CrudRepository;

public interface DatesBookedRepository extends CrudRepository<DatesBookedInterval,Reservation> {
    List<DatesBookedInterval> findByReservation(Reservation reservation);
}
