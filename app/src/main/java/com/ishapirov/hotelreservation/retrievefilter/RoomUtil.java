package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelapi.services.reservation.exceptions.ReservationOverlapException;
import com.ishapirov.hotelapi.services.room.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.services.room.paramvalidation.OneRoomCriteria;
import com.ishapirov.hotelapi.services.room.paramvalidation.RoomsCriteria;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RoomUtil {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    RoomTypeUtil roomTypeUtil;

    public Room getRoomByRoomNumber(Integer roomNumber){
        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        if(room.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
        return room.get();
    }

    public Room getRoomByRoomNumberAndDates(Integer roomNumber, Date checkIn, Date checkOut){
        confirmRoomExists(roomNumber);
        return getRoomIfAvailable(roomNumber,checkIn,checkOut);
    }

    public Room getRoomExcludingReservation(Integer roomNumber,Date checkIn,Date checkOut,Integer reservationNumber){
        confirmRoomExists(roomNumber);
        Optional<Room> room = roomRepository.findByRoomNumberIfAvailableExcludingOneReservation(checkIn,checkOut,roomNumber,reservationNumber);
        if(room.isEmpty())
            throw new ReservationOverlapException("The room has already been reserved for the given date interval");
        return room.get();
    }

    public Room getRoomIfAvailable(Integer roomNumber,Date checkIn,Date checkOut){
        validateDates(checkIn,checkOut);
        Optional<Room> room = roomRepository.findByRoomNumberIfAvailable(checkIn,checkOut,roomNumber);
        if(room.isEmpty())
            throw new ReservationOverlapException("The room has already been reserved for the given date interval");
        return room.get();
    }

    public void confirmRoomExists(Integer roomNumber){
        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        if(room.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
    }

    public void validateDates(Date checkInDate,Date checkOutDate){
        if((checkInDate == null && checkOutDate != null) ||(checkInDate != null && checkOutDate == null))
            throw new BadRequestException("Only a check in date or check out date have been provided, but not both.");
        if(checkInDate.compareTo(new Date())<0)
            throw new DatesInvalidException("The check in date has been set for a date in the past");
        if(checkOutDate.compareTo(checkInDate)<0)
            throw new DatesInvalidException("The check out date is before the check in date");
    }

    public Room getRoomBasedOnCriteria(Integer roomNumber, OneRoomCriteria oneRoomCriteria){

        if(oneRoomCriteria.getCheckInDate() != null || oneRoomCriteria.getCheckOutDate() != null){
            return getRoomIfAvailable(roomNumber,oneRoomCriteria.getCheckInDate(),oneRoomCriteria.getCheckOutDate());
        } else {
                return getRoomByRoomNumber(roomNumber);
        }
    }

    public Page<Room> getRoomsBasedOnCriteria(RoomsCriteria roomsCriteria, Pageable pageable){
        if(roomsCriteria.getCheckInDate() != null || roomsCriteria.getCheckOutDate() != null){
           validateDates(roomsCriteria.getCheckInDate(), roomsCriteria.getCheckOutDate());
            if(roomsCriteria.getRoomType() != null) {
                RoomType roomtype = roomTypeUtil.getRoomType(roomsCriteria.getRoomType());
                return roomRepository.findAllAvailableAndRoomType(roomsCriteria.getCheckInDate(), roomsCriteria.getCheckOutDate(), roomtype, pageable);
            } else {
                return roomRepository.findAllAvailable(roomsCriteria.getCheckInDate(), roomsCriteria.getCheckOutDate(),pageable);
            }
        } else {
            if(roomsCriteria.getRoomType() != null) {
                RoomType roomtype = roomTypeUtil.getRoomType(roomsCriteria.getRoomType());
                return roomRepository.findAllByRoomType(roomtype, pageable);
            } else {
                return roomRepository.findAll(pageable);
            }
        }
    }

    public void saveRoom(Room room){
        roomRepository.save(room);
    }
}
