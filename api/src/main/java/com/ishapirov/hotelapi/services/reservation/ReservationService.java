package com.ishapirov.hotelapi.services.reservation;

import com.ishapirov.hotelapi.generalexceptions.BadRequestException;
import com.ishapirov.hotelapi.generalexceptions.DatesInvalidException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.reservation.domain.*;
import com.ishapirov.hotelapi.services.reservation.domain.admin.BookReservationForUser;
import com.ishapirov.hotelapi.services.reservation.exceptions.*;
import com.ishapirov.hotelapi.services.reservation.paramvalidation.ReservationsCriteria;
import com.ishapirov.hotelapi.services.room.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.services.user.exceptions.UserNotFoundException;
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
    ReservationInformation bookReservation(@RequestBody @Valid BookReservation bookReservation)
            throws UserNotFoundException, RoomNotFoundException, ReservationOverlapException, BadRequestException, DatesInvalidException;

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
    ReservationInformation bookReservationForUser(@RequestBody @Valid BookReservationForUser bookReservationForUser)
            throws UserNotFoundException, RoomNotFoundException, ReservationOverlapException, BadRequestException, DatesInvalidException;

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin/cancel")
    ReservationInformation cancelReservationForUser(@RequestBody @Valid CancelReservation cancelReservation);
}
