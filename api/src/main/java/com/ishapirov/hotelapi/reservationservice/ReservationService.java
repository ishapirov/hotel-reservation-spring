package com.ishapirov.hotelapi.reservationservice;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.reservationservice.domain.*;
import com.ishapirov.hotelapi.reservationservice.domain.admin.BookRoomForCustomer;
import com.ishapirov.hotelapi.reservationservice.domain.admin.CancelReservationForCustomer;
import com.ishapirov.hotelapi.reservationservice.exceptions.*;
import com.ishapirov.hotelapi.reservationservice.paramvalidation.ReservationsCriteria;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.userservice.exceptions.CustomerNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/services/reservations")
public interface ReservationService {

    @GetMapping
    HotelPage<ReservationBasicInformation> getReservations(@Valid ReservationsCriteria reservationsCriteria);

    @GetMapping("/{reservationNumber}")
    ReservationInformation getReservation(@PathVariable @Positive Integer reservationNumber)
            throws ReservationNotFoundException, ReservationUsernamesDoNotMatchException;

    @PostMapping
    ReservationInformation bookReservation(@RequestBody @Valid BookRoom bookRoom)
            throws CustomerNotFoundException, RoomNotFoundException, ReservationOverlapException, BadRequestException, DatesInvalidException;

    @PutMapping("/{reservationNumber}")
    ReservationInformation updateReservation(@PathVariable @Positive Integer reservationNumber,
                                             @RequestBody @Valid ReservationUpdate reservationUpdate);

    @DeleteMapping("/{reservationNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReservation(@PathVariable @Positive Integer reservationNumber)
            throws ReservationNotFoundException, ReservationDeletionError;

    @PostMapping("/cancel")
    ReservationInformation cancelReservation(@RequestBody @Valid CancelReservation cancelReservation)
            throws ReservationNotFoundException, ReservationAlreadyCancelledException;

    @GetMapping("/response/{reservationNumber}")
    ReservationResponse getReservationResponse(@PathVariable @Positive Integer reservationNumber);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin")
    ReservationInformation bookRoomForCustomer(@RequestBody @Valid BookRoomForCustomer bookRoomForCustomer)
            throws CustomerNotFoundException, RoomNotFoundException, ReservationOverlapException, BadRequestException, DatesInvalidException;

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin/cancel")
    ReservationInformation cancelRoomForCustomer(@RequestBody @Valid CancelReservationForCustomer cancelReservation);
}
