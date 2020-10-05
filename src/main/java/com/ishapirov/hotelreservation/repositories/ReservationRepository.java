package com.ishapirov.hotelreservation.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelreservation.hotel_objects.Customer;
import com.ishapirov.hotelreservation.hotel_objects.Reservation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation,Integer> {
    Optional<Reservation>findByRegistrationNumber(Integer registrationNumber);
    List<Reservation> findByCustomer(Customer customer);

    // Doesn't work :(
    // @Query("SELECT COUNT(res) FROM Reservation res WHERE res.r_num = ?1 AND ((res.checkin > ?1 AND res.checkin < ?2) OR (res.checkout < ?2 AND res.checkout > ?1))")
    // Integer isRoomAvailable(Integer roomNumber,Date checkIn, Date checkOut);
}
