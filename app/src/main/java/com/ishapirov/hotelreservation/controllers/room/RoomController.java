package com.ishapirov.hotelreservation.controllers.room;

import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.room.paramvalidation.OneRoomCriteria;
import com.ishapirov.hotelapi.services.room.paramvalidation.RoomsCriteria;
import com.ishapirov.hotelapi.services.room.domain.RoomUpdate;
import com.ishapirov.hotelapi.services.room.RoomService;
import com.ishapirov.hotelapi.services.room.domain.RoomBasicInformation;
import com.ishapirov.hotelapi.services.room.domain.RoomInformation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.RoomType;
import com.ishapirov.hotelreservation.mapper.DomainToApiMapper;
import com.ishapirov.hotelreservation.retrievefilter.RoomTypeUtil;
import com.ishapirov.hotelreservation.retrievefilter.RoomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class RoomController implements RoomService {

    @Autowired
    private DomainToApiMapper domainToApiMapper;
    @Autowired
    private RoomUtil roomUtil;
    @Autowired
    private RoomTypeUtil roomTypeUtil;

    @Override
    public HotelPage<RoomBasicInformation> getRooms(RoomsCriteria roomsCriteria) {
        Pageable pageable = PageRequest.of(roomsCriteria.getPageNumber(), roomsCriteria.getSize(), Sort.by("roomNumber"));
        Page<Room> rooms = roomUtil.getRoomsBasedOnCriteria(roomsCriteria,pageable);
        return domainToApiMapper.getBasicRoomsInformation(rooms,pageable);
    }

    @Override
    public RoomInformation getRoom(Integer roomNumber, OneRoomCriteria oneRoomCriteria) {
        Room room = roomUtil.getRoomBasedOnCriteria(roomNumber,oneRoomCriteria);
        return domainToApiMapper.getRoomInformation(room);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RoomInformation updateRoom(Integer roomNumber, RoomUpdate roomUpdate) {
        Room room = roomUtil.getRoomByRoomNumber(roomNumber);
        room.setRoomPrice(roomUpdate.getRoomPrice());
        if(roomUpdate.getRoomType() != null){
            RoomType roomType = roomTypeUtil.getRoomType(roomUpdate.getRoomType());
            room.setRoomType(roomType);
        }
        roomUtil.saveRoom(room);
        return domainToApiMapper.getRoomInformation(room);
    }
}
