package com.ishapirov.hotelreservation.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


import com.ishapirov.hotelapi.*;
import com.ishapirov.hotelapi.exceptions.*;

import com.ishapirov.hotelreservation.repositories.CustomerRepository;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import com.ishapirov.hotelreservation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelController implements HotelAPIInterface {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/test")
    public Response test() {
        return new Response("Test successful!");
    }

    @GetMapping("/testToken")
    public String testToken() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername() + userDetails.getAuthorities();
    }

    @Transactional
    @PostMapping("/signup")
    public Response signup(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return new Response("Sign in successful");
    }

    @PostMapping("/authenticate")
    public TokenClass generateToken(@RequestBody CustomerCredentials customerCredentials) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    customerCredentials.getUsername(), customerCredentials.getPassword()));
        } catch (Exception ex) {
            throw new InvalidUsernameOrPasswordException();
        }
        return new TokenClass(jwtUtil.generateToken(customerCredentials.getUsername()));
    }

    @GetMapping("/getroom")
    public Room getRoom(@RequestParam(required = true) Integer roomNumber) {
        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        if(!room.isPresent())
            throw new RoomNotFoundException();
        return room.get();
    }

    @GetMapping("/getallrooms")
    public List<Room> getRoomsByType(@RequestParam(required = false) String roomType) {
        if(roomType == null)
            return roomRepository.findAll();
        Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
        if (!getRoomType.isPresent())
            throw new RoomTypeNotFoundException();
        return roomRepository.findByRoomType(getRoomType.get());
    }

    @GetMapping("/getavailablerooms")
    public List<Room> getAvailableRooms(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                        @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                                        @RequestParam(required = false) String roomType){
        List<Room> rooms;
        if(roomType == null){
            rooms = roomRepository.findAll();
        }
        else{
            Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
            if (!getRoomType.isPresent())
                throw new RoomTypeNotFoundException();
            rooms = roomRepository.findByRoomType(getRoomType.get());
        }
        return returnRooms(rooms, checkInDate,checkOutDate);
    }

    @GetMapping("/getroomifavailable")
    public Room getRoomIfAvailable(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                   @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                                   @RequestParam(required = false) Integer roomNumber) {

        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        if(!room.isPresent())
            throw new RoomNotFoundException();
        List<Reservation> reservations = reservationRepository.findByRoom(room.get());
        if(!isDateAvailable(reservations, checkInDate, checkOutDate))
            throw new ReservationOverlapException();
        return room.get();
    }

    public List<Room> returnRooms(List<Room> rooms,Date checkinDate,Date checkoutDate) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms){
            List<Reservation> reservations = reservationRepository.findByRoom(room);
            if(isDateAvailable(reservations, checkinDate, checkoutDate))
                availableRooms.add(room);

        }
        return availableRooms;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/bookRoomForCustomer")
    public Reservation bookRoomForCustomer(@RequestBody BookRoomForCustomer bookRoomForCustomer) {
        Optional<Customer> customer = customerRepository.findByUsername(bookRoomForCustomer.getUsername());
        Optional<Room> room = roomRepository.findByRoomNumber(bookRoomForCustomer.getRoomNumber());
        if(!customer.isPresent())
            throw new CustomerNotFoundException();
        if(!room.isPresent())
            throw new RoomNotFoundException();
        List<Reservation> reservations = reservationRepository.findByRoom(room.get());
        if(!isDateAvailable(reservations, bookRoomForCustomer.getCheckInDate(), bookRoomForCustomer.getCheckOutDate())){
            throw new ReservationOverlapException();
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setDateCheckIn(bookRoomForCustomer.getCheckInDate());
        reservation.setDateCheckOut(bookRoomForCustomer.getCheckOutDate());
        reservationRepository.save(reservation);
        return reservation;

    }

    @PostMapping("/bookroom")
    public Reservation bookRoom(@RequestBody BookRoom bookRoom) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        BookRoomForCustomer bookRoomForCustomer = new BookRoomForCustomer(username,bookRoom);
        return bookRoomForCustomer(bookRoomForCustomer);
    }

    private boolean isDateAvailable(List<Reservation> reservations,Date checkin, Date checkout){
        for(Reservation reservation: reservations){
            if(!reservation.isAvailable(checkin, checkout)){
                return false;
            }
        }
        return true;
    }

    @PostMapping("/cancelreservation")
    @Transactional
    public Response cancelRoom(@RequestBody CancelReservation cancelReservation){
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(cancelReservation.getReservationNumber());
        if(!reservation.isPresent())
            return new Response("Reservation not found");
        if(reservationRepository.deleteByReservationNumber(cancelReservation.getReservationNumber()) != 1)
            return new Response("An error occured cancelling the reservation");
        return new Response("Reservation successfully cancelled");
    }

    @GetMapping("/viewreservation")
    public Reservation viewReservation(@RequestParam(required = true) Integer reservationNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(!reservation.isPresent())
            throw new ReservationNotFoundException();
        Reservation userReservation = reservation.get();
        if(!userDetails.getUsername().equals(userReservation.getCustomer().getUsername()))
            throw new ReservationUsernamesDoNotMatchException();
        return userReservation;
    }

}
