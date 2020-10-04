package com.ishapirov.hotelreservation.repositories;

import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelreservation.hotel_objects.Customer;
import com.ishapirov.hotelreservation.hotel_objects.Reservation;

import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation,Integer> {
    Optional<Reservation>findByRegistrationNumber(Integer registrationNumber);
    List<Reservation> findByCustomer(Customer customer);
}
