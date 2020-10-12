package com.ishapirov.hotelapi;

import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface HotelAPIInterface {

    @GetMapping("/test")
    Response test();

    @GetMapping("/testToken")
    String testToken();

    @PostMapping("/signup")
    Response signup(@RequestBody CustomerSignupInformation customer);

    @PostMapping("/authenticate")
    TokenClass generateToken(@RequestBody CustomerCredentials customerCredentials);

    @GetMapping("/getroom")
    RoomInformation getRoom(@RequestParam(required = true) Integer roomNumber);

    @GetMapping("/getallrooms")
    List<RoomInformation> getRoomsByType(@RequestParam(required = false) String roomType);

    @GetMapping("/getavailablerooms")
    List<RoomInformation> getAvailableRooms(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                 @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                                 @RequestParam(required = false) String roomType);

    @GetMapping("/getroomifavailable")
    RoomInformation getRoomIfAvailable(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                            @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                            @RequestParam(required = false) Integer roomNumber);

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @PostMapping("/bookRoomForCustomer")
    ReservationInformation bookRoomForCustomer(@RequestBody BookRoomForCustomer bookRoomForCustomer);

    @PostMapping("/bookroom")
    ReservationInformation bookRoom(@RequestBody BookRoom bookRoom);

    @PostMapping("/cancelreservation")
    Response cancelRoom(@RequestBody CancelReservation cancelReservation);

    @GetMapping("/viewreservation")
    ReservationInformation viewReservation(@RequestParam(required = true) Integer reservationNumber);

}
