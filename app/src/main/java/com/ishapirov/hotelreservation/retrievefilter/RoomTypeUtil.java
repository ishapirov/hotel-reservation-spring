package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelapi.services.room.exceptions.RoomTypeNotFoundException;
import com.ishapirov.hotelreservation.domain.RoomType;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomTypeUtil {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public RoomType getRoomType(String roomTypeName){
        Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomTypeName);
        if(getRoomType.isEmpty())
            throw new RoomTypeNotFoundException("A room type with the given room type name was not found");
        return getRoomType.get();
    }
}
