package com.ishapirov.hotelreservation.repositories;

import java.util.Optional;

import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RoomRepository extends PagingAndSortingRepository<Room,Integer> {
    Optional<Room> findByRoomNumber(int roomNumber);
    Page<Room> findAllByRoomType(RoomType roomType, Pageable page);
    Page<Room> findAll(org.springframework.data.domain.Pageable page);
}
