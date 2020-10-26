package com.ishapirov.hotelreservation.repositories;

import java.util.Date;
import java.util.Optional;

import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RoomRepository extends PagingAndSortingRepository<Room,Integer> {
    Optional<Room> findByRoomNumber(int roomNumber);
    Page<Room> findAllByRoomType(RoomType roomType, Pageable page);
    Page<Room> findAll(org.springframework.data.domain.Pageable page);

    @Query("SELECT r FROM Room r WHERE r NOT IN " +
            "(SELECT res.room FROM Reservation res WHERE " +
            "((res.checkInDate >= ?1 AND res.checkInDate < ?2)" +
            "OR (res.checkOutDate > ?1 AND res.checkOutDate <= ?2)" +
            "OR (res.checkInDate <= ?1 AND res.checkOutDate >= ?2)))")
    Page<Room> findAllAvailable(Date checkInDate, Date checkOutDate, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.roomType = ?3 AND r NOT IN " +
            "(SELECT res.room FROM Reservation res WHERE " +
            "((res.checkInDate >= ?1 AND res.checkInDate < ?2)" +
            "OR (res.checkOutDate > ?1 AND res.checkOutDate <= ?2)" +
            "OR (res.checkInDate <= ?1 AND res.checkOutDate >= ?2)))")
    Page<Room> findAllAvailableAndRoomType(Date checkInDate, Date checkOutDate,RoomType roomType, Pageable pageable);
}
