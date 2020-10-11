package com.ishapirov.hotelreservation.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;


import com.ishapirov.hotelapi.*;
import com.ishapirov.hotelapi.exceptions.*;

import com.ishapirov.hotelreservation.hotelclasses.Customer;
import com.ishapirov.hotelreservation.hotelclasses.Reservation;
import com.ishapirov.hotelreservation.hotelclasses.Room;
import com.ishapirov.hotelreservation.hotelclasses.RoomType;
import com.ishapirov.hotelreservation.repositories.CustomerRepository;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import com.ishapirov.hotelreservation.util.HotelUtil;
import com.ishapirov.hotelreservation.util.JwtUtil;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private HotelUtil hotelUtil;

    private DozerBeanMapper mapper = new DozerBeanMapper();

    @Override
    public Response test() {
        return new Response("Test successful!");
    }

    @Override
    public String testToken() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername() + userDetails.getAuthorities();
    }


    @Override
    @Transactional
    public Response signup(@RequestBody CustomerInformation customerInformation) {
        Customer customer = new Customer(customerInformation);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return new Response("Sign in successful");
    }

    @Override
    public TokenClass generateToken(@RequestBody CustomerCredentials customerCredentials) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    customerCredentials.getUsername(), customerCredentials.getPassword()));
        } catch (Exception ex) {
            throw new InvalidUsernameOrPasswordException();
        }
        return new TokenClass(jwtUtil.generateToken(customerCredentials.getUsername()));
    }

    @Override
    public RoomInformation getRoom(@RequestParam(required = true) Integer roomNumber) {
        Optional<Room> getRoom = roomRepository.findByRoomNumber(roomNumber);
        if(getRoom.isEmpty())
            throw new RoomNotFoundException();
        Room room = getRoom.get();
        RoomInformation roomInformation = new RoomInformation();
        roomInformation.setRoomNumber(room.getRoomNumber());
        if(roomInformation.getRoomPrice() == null)
            roomInformation.setRoomPrice(room.getRoomType().getDefaultPrice());
        else
            roomInformation.setRoomPrice(room.getRoomPrice());
        roomInformation.setRoomType(room.getRoomType().getName());
        return roomInformation;
    }

    @Override
    public List<RoomInformation> getRoomsByType(@RequestParam(required = false) String roomType) {
        if(roomType == null)
            return roomRepository
                    .findAll()
                    .stream()
                    .map(room -> new RoomInformation(
                            room.getRoomNumber(),
                            room.getRoomType().getName(),
                            room.getRoomPrice()
            )).collect(Collectors.toList());
        Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
        if (getRoomType.isEmpty())
            throw new RoomTypeNotFoundException();
        return roomRepository.findByRoomType(getRoomType.get()).stream().map(room -> new RoomInformation(room)).collect(Collectors.toList());
    }

    @Override
    public List<RoomInformation> getAvailableRooms(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                        @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                                        @RequestParam(required = false) String roomType){
        List<Room> rooms;
        if(roomType == null){
            rooms = roomRepository.findAll();
        }
        else{
            Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
            if (getRoomType.isEmpty())
                throw new RoomTypeNotFoundException();
            rooms = roomRepository.findByRoomType(getRoomType.get());
        }
        return hotelUtil.returnRooms(rooms, checkInDate,checkOutDate).stream().map(room -> new RoomInformation(room)).collect(Collectors.toList());
    }

    @Override
    public RoomInformation getRoomIfAvailable(@RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkInDate,
                                   @RequestParam(required = true) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Date checkOutDate,
                                   @RequestParam(required = false) Integer roomNumber) {

        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        if(room.isEmpty())
            throw new RoomNotFoundException();
        List<Reservation> reservations = reservationRepository.findByRoom(room.get());
        if(!hotelUtil.isDateAvailable(reservations, checkInDate, checkOutDate))
            throw new ReservationOverlapException();
        return new RoomInformation(room.get());
    }


    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReservationInformation bookRoomForCustomer(@RequestBody BookRoomForCustomer bookRoomForCustomer) {
        Optional<Customer> customer = customerRepository.findByUsername(bookRoomForCustomer.getUsername());
        Optional<Room> room = roomRepository.findByRoomNumber(bookRoomForCustomer.getRoomNumber());
        if(customer.isEmpty())
            throw new CustomerNotFoundException();
        if(room.isEmpty())
            throw new RoomNotFoundException();
        List<Reservation> reservations = reservationRepository.findByRoom(room.get());
        if(!hotelUtil.isDateAvailable(reservations, bookRoomForCustomer.getCheckInDate(), bookRoomForCustomer.getCheckOutDate())){
            throw new ReservationOverlapException();
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setDateCheckIn(bookRoomForCustomer.getCheckInDate());
        reservation.setDateCheckOut(bookRoomForCustomer.getCheckOutDate());
        reservationRepository.save(reservation);
        return new ReservationInformation(reservation);

    }

    @Override
    public ReservationInformation bookRoom(@RequestBody BookRoom bookRoom) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        BookRoomForCustomer bookRoomForCustomer = new BookRoomForCustomer(username,bookRoom);
        return bookRoomForCustomer(bookRoomForCustomer);
    }



    @Override
    @Transactional
    public Response cancelRoom(@RequestBody CancelReservation cancelReservation){
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(cancelReservation.getReservationNumber());
        if(reservation.isEmpty())
            return new Response("Reservation not found");
        if(reservationRepository.deleteByReservationNumber(cancelReservation.getReservationNumber()) != 1)
            return new Response("An error occured cancelling the reservation");
        return new Response("Reservation successfully cancelled");
    }

    @Override
    public ReservationInformation viewReservation(@RequestParam(required = true) Integer reservationNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(!reservation.isPresent())
            throw new ReservationNotFoundException();
        Reservation userReservation = reservation.get();
        if(!userDetails.getUsername().equals(userReservation.getCustomer().getUsername()))
            throw new ReservationUsernamesDoNotMatchException();
        return new ReservationInformation(userReservation);
    }

}
