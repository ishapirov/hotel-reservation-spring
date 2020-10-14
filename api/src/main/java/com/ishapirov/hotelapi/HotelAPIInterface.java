package com.ishapirov.hotelapi;

import java.util.Date;
import java.util.List;

import com.ishapirov.hotelapi.cancel.CancelReservation;
import com.ishapirov.hotelapi.domainapi.CustomerInformation;
import com.ishapirov.hotelapi.domainapi.ReservationInformation;
import com.ishapirov.hotelapi.domainapi.RoomInformation;
import com.ishapirov.hotelapi.formdata.BookRoom;
import com.ishapirov.hotelapi.formdata.adminreq.BookRoomForCustomer;
import com.ishapirov.hotelapi.formdata.CustomerCredentials;
import com.ishapirov.hotelapi.formdata.CustomerSignupInformation;
import com.ishapirov.hotelapi.token.TokenClass;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface HotelAPIInterface {

    @GetMapping("/test")
    Response test();

    @GetMapping("/testToken")
    String testToken();

    @PostMapping("/signup")
    CustomerInformation signup(@RequestBody CustomerSignupInformation customer);

    @PostMapping("/authenticate")
    TokenClass generateToken(@RequestBody CustomerCredentials customerCredentials);

    @GetMapping("/getcustomer")
    CustomerInformation getCustomer(@RequestParam String username);

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancelRoom(@RequestBody CancelReservation cancelReservation);

    @GetMapping("/viewreservation")
    ReservationInformation viewReservation(@RequestParam(required = true) Integer reservationNumber);

}
