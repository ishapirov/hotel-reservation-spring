package com.ishapirov.hotelapi;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface HotelAPIInterface {

    @GetMapping("/test")
    Response test();

    @GetMapping("/testToken")
    String testToken();

    @Transactional
    @PostMapping("/signup")
    Response signup(@RequestBody Customer customer);

    @PostMapping("/authenticate")
    TokenClass generateToken(@RequestBody CustomerCredentials customerCredentials);

    @GetMapping("/getroom")
    Room getRoom(@RequestParam(required = true) Integer roomNumber);

    @GetMapping("/getallrooms")
    List<Room> getRoomsByType(@RequestParam(required = false) String roomType);

    @GetMapping("/getavailablerooms")
    List<Room> getAvailableRooms(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                 @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                                 @RequestParam(required = false) String roomType);

    @GetMapping("/getroomifavailable")
    Room getRoomIfAvailable(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                            @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                            @RequestParam(required = false) Integer roomNumber);

    List<Room> returnRooms(List<Room> rooms,Date checkinDate,Date checkoutDate);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/bookRoomForCustomer")
    @Transactional
    Reservation bookRoomForCustomer(@RequestBody BookRoomForCustomer bookRoomForCustomer);

    @PostMapping("/bookroom")
    @Transactional
    Reservation bookRoom(@RequestBody BookRoom bookRoom);

    @PostMapping("/cancelreservation")
    @Transactional
    Response cancelRoom(@RequestBody CancelReservation cancelReservation);

    @GetMapping("/viewreservation")
    Reservation viewReservation(@RequestParam(required = true) Integer reservationNumber);

}
