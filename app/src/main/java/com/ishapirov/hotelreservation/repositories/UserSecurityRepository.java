package com.ishapirov.hotelreservation.repositories;

import com.ishapirov.hotelreservation.domain.UserSecurity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserSecurityRepository extends CrudRepository<UserSecurity,String> {
    Optional<UserSecurity> findByUsername(String username);
}
