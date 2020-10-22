package com.ishapirov.hotelapi.reservationservice;

import com.ishapirov.hotelapi.reservationservice.domain.*;
import com.ishapirov.hotelapi.reservationservice.domain.admin.BookRoomForCustomer;
import com.ishapirov.hotelapi.reservationservice.domain.admin.CancelReservationForCustomer;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequestMapping("/services/reservations")
public interface ReservationService {

    @GetMapping
    Page<ReservationInformation> getReservations(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer pageNumber,
                                                 @RequestParam(required = false, defaultValue = "10") @PositiveOrZero @Max(100) Integer size,
                                                 @RequestParam(required = false) String username);

    @GetMapping("/{reservationNumber}")
    ReservationInformation getReservation(@PathVariable @Positive Integer reservationNumber);

    @PostMapping
    ReservationInformation bookReservation(@RequestBody @Valid BookRoom bookRoom);

    @PutMapping("/{reservationNumber}")
    ReservationInformation updateReservation(@PathVariable @Positive Integer reservationNumber,
                                             @RequestBody @Valid ReservationUpdate reservationUpdate);

    @DeleteMapping("/{reservationNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReservation(@PathVariable @Positive Integer reservationNumber);

    @PostMapping("/cancel")
    ReservationInformation cancelReservation(@RequestBody @Valid CancelReservation cancelReservation);

    @GetMapping("/response/{reservationNumber}")
    ReservationResponse getReservationResponse(@PathVariable @Positive Integer reservationNumber);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin")
    ReservationInformation bookRoomForCustomer(@RequestBody @Valid BookRoomForCustomer bookRoomForCustomer);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin/cancel")
    ReservationInformation cancelRoomForCustomer(@RequestBody @Valid CancelReservationForCustomer cancelReservation);
}
