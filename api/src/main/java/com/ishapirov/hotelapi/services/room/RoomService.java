package com.ishapirov.hotelapi.services.room;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.reservation.exceptions.ReservationOverlapException;
import com.ishapirov.hotelapi.services.room.domain.RoomBasicInformation;
import com.ishapirov.hotelapi.services.room.domain.RoomInformation;
import com.ishapirov.hotelapi.services.room.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.services.room.exceptions.RoomTypeNotFoundException;
import com.ishapirov.hotelapi.services.room.paramvalidation.OneRoomCriteria;
import com.ishapirov.hotelapi.services.room.paramvalidation.RoomsCriteria;
import com.ishapirov.hotelapi.services.room.domain.RoomUpdate;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

@RestController
@Validated
@RequestMapping("/services/rooms")
public interface RoomService {

    @GetMapping
    HotelPage<RoomBasicInformation> getRooms(@Valid RoomsCriteria roomsCriteria)
            throws BadRequestException, DatesInvalidException, RoomTypeNotFoundException;

    @GetMapping("/{roomNumber}")
    RoomInformation getRoom(@PathVariable  @Min(1985) @Max(2084) Integer roomNumber,
                            @Valid OneRoomCriteria oneRoomCriteria)
            throws BadRequestException, DatesInvalidException, RoomTypeNotFoundException, RoomNotFoundException, ReservationOverlapException;;

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PutMapping("/{roomNumber}")
    RoomInformation updateRoom(@PathVariable  @Min(1985) @Max(2084) @Positive Integer roomNumber,
                               @RequestBody @Valid RoomUpdate roomUpdate);
}
