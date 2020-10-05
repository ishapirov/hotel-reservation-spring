package com.ishapirov.hotelreservation.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ishapirov.hotelreservation.hotel_objects.Room;
import com.ishapirov.hotelreservation.hotel_objects.RoomType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room,Integer>{
    Optional<Room> findByRoomNumber(int roomNumber);
    List<Room> findByRoomType(RoomType roomType);
    List<Room> findAll();

    // Doesn't work :(
    // @Query("SELECT room FROM Room room, Reservation reserv WHERE r.room_type = ?1 AND reserv.r_num = room.room_number AND reserv.checkout < ?2 OR reserv.checkin > ?3")
    // List<Room> findAllAvailableType(String roomType,Date checkIn,Date checkOut);
    // @Query("SELECT room FROM Room room, Reservation reserv WHERE reserv.r_num = room.room_number AND reserv.checkout < ?1 OR reserv.checkin > ?2")
    // List<Room> findAllAvailableNoType(Date checkIn,Date checkOut);
}
