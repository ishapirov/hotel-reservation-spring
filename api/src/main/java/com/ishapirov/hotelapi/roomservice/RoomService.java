package com.ishapirov.hotelapi.roomservice;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.reservationservice.exceptions.ReservationOverlapException;
import com.ishapirov.hotelapi.roomservice.domain.RoomBasicInformation;
import com.ishapirov.hotelapi.roomservice.domain.RoomInformation;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomTypeNotFoundException;
import com.ishapirov.hotelapi.roomservice.paramvalidation.OneRoomCriteria;
import com.ishapirov.hotelapi.roomservice.paramvalidation.RoomsCriteria;
import com.ishapirov.hotelapi.roomservice.domain.RoomUpdate;

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
