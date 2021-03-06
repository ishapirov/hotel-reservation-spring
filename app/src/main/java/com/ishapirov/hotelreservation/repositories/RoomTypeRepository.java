package com.ishapirov.hotelreservation.repositories;

import java.util.Optional;

import com.ishapirov.hotelreservation.domain.RoomType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends CrudRepository<RoomType,String>{
    
    Optional<RoomType> findByName(String id);
    
}