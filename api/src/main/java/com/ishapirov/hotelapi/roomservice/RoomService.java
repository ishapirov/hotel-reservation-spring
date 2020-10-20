package com.ishapirov.hotelapi.roomservice;

import com.ishapirov.hotelapi.roomservice.domain.BasicRoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@RestController
@Validated
@RequestMapping("/services/rooms")
public interface RoomService {

    @GetMapping
    List<BasicRoomInformation> getRooms(@RequestParam(required = false) @Size(max=32) String roomType,
                                              @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) @FutureOrPresent Date checkInDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) @FutureOrPresent Date checkOutDate);

    @GetMapping("/{roomNumber}")
    RoomInformation getRoom(@PathVariable  @Min(1985) @Max(2084) Integer roomNumber,
                            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) @FutureOrPresent Date checkInDate,
                            @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) @FutureOrPresent Date checkOutDate);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PutMapping("/{roomNumber}")
    RoomInformation updateRoom(@PathVariable  @Min(1985) @Max(2084) @Positive Integer roomNumber, @RequestBody @Valid RoomUpdate roomUpdate);
}
