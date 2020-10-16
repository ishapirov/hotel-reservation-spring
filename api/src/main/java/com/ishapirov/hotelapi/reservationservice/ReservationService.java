package com.ishapirov.hotelapi.reservationservice;

import com.ishapirov.hotelapi.reservationservice.domain.ReservationInformation;
import com.ishapirov.hotelapi.reservationservice.domain.BookRoom;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationResponse;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationUpdate;
import com.ishapirov.hotelapi.reservationservice.domain.admin.BookRoomForCustomer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services/reservations")
public interface ReservationService {

    @GetMapping
    List<ReservationInformation> getReservations();

    @GetMapping("/{reservationNumber}")
    ReservationInformation getReservation(@PathVariable Integer reservationNumber);

    @PostMapping
    ReservationInformation bookReservation(@RequestBody BookRoom bookRoom);

    @PutMapping("/{reservationNumber}")
    ReservationInformation updateReservation(@PathVariable Integer reservationNumber,
                                             @RequestBody ReservationUpdate reservationUpdate);

    @DeleteMapping("/{reservationNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReservation(@PathVariable Integer reservationNumber);

    @PostMapping("/cancel")
    ReservationInformation cancelReservation(@RequestBody CancelReservation cancelReservation);

    @GetMapping("/response/{reservationNumber}")
    ReservationResponse getReservationResponse(@PathVariable Integer reservationNumber);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin")
    ReservationInformation bookRoomForCustomer(@RequestBody BookRoomForCustomer bookRoomForCustomer);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/admin/cancel")
    ReservationInformation cancelRoomForCustomer(@RequestBody CancelReservation cancelReservation);
}
