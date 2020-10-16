package com.ishapirov.hotelreservation.controllers.room;

import com.ishapirov.hotelapi.generalexceptions.NotImplementedException;
import com.ishapirov.hotelapi.reservationservice.exceptions.ReservationOverlapException;
import com.ishapirov.hotelapi.roomservice.domain.RoomUpdate;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomTypeNotFoundException;
import com.ishapirov.hotelapi.roomservice.RoomService;
import com.ishapirov.hotelapi.roomservice.domain.BasicRoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import com.ishapirov.hotelreservation.util.DomainToApiMapper;
import com.ishapirov.hotelreservation.util.HotelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class RoomController implements RoomService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private DomainToApiMapper domainToApiMapper;
    @Autowired
    private HotelUtil hotelUtil;

    @Override
    public List<BasicRoomInformation> getRooms(String roomType, Date checkInDate, Date checkOutDate) {
        List<Room> rooms;
        if(roomType == null) {
            rooms = roomRepository.findAll();
        }
        else {
            Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
            if (getRoomType.isEmpty())
                throw new RoomTypeNotFoundException("A room type with the given room type name was not found");
            rooms = roomRepository.findByRoomType(getRoomType.get());
        }
        if(checkInDate != null || checkOutDate != null){
            hotelUtil.validateDates(checkInDate, checkOutDate);
            rooms = hotelUtil.filterRooms(rooms,checkInDate,checkOutDate);
        }
        return domainToApiMapper.getBasicRoomsInformation(rooms);
    }

    @Override
    public RoomInformation getRoom(Integer roomNumber, Date checkInDate, Date checkOutDate) {
        Optional<Room> getRoom = roomRepository.findByRoomNumber(roomNumber);
        if(getRoom.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
        if(checkInDate != null || checkOutDate != null){
            hotelUtil.validateDates(checkInDate,checkOutDate);
            List<Reservation> reservations = reservationRepository.findByRoom(getRoom.get());
            if(!hotelUtil.isDateAvailable(reservations, checkInDate, checkOutDate))
                throw new ReservationOverlapException("The room has already been reserved for the given date interval");
        }
        return domainToApiMapper.getRoomInformation(getRoom.get());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoomInformation updateRoom(Integer roomNumber, RoomUpdate roomUpdate) {
        throw new NotImplementedException("This operation is not yet supported");
    }
}
