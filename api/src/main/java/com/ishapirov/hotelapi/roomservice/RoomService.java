package com.ishapirov.hotelapi.roomservice;

import com.ishapirov.hotelapi.roomservice.domain.BasicRoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomResponse;
import com.ishapirov.hotelapi.roomservice.domain.RoomUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/services/rooms")
public interface RoomService {

    @GetMapping
    List<BasicRoomInformation> getRooms(@RequestParam(required = false) String roomType,
                                              @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate);

    @GetMapping("/{roomNumber}")
    RoomInformation getRoom(@PathVariable Integer roomNumber,
                            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PutMapping("/{roomNumber}")
    RoomInformation updateRoom(@PathVariable Integer roomNumber, @RequestBody RoomUpdate roomUpdate);
}
