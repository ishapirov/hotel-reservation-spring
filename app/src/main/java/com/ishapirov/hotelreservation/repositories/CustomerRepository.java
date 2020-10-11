package com.ishapirov.hotelreservation.repositories;

import java.util.Optional;

import com.ishapirov.hotelreservation.hotelclasses.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {
    Optional<Customer> findByUsername(String username);
}