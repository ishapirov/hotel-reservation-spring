package com.ishapirov.hotelreservation.repositories;

import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelreservation.hotel_objects.Room;
import com.ishapirov.hotelreservation.hotel_objects.RoomType;

import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room,Integer>{
    Optional<Room> findByRoomNumber(int roomNumber);
    List<Room> findByRoomType(RoomType roomType);
    List<Room> findAll();
}
