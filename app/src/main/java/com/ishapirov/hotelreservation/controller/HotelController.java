package com.ishapirov.hotelreservation.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


import com.ishapirov.hotelapi.*;
import com.ishapirov.hotelapi.cancel.CancelReservation;
import com.ishapirov.hotelapi.domainapi.CustomerInformation;
import com.ishapirov.hotelapi.domainapi.ReservationInformation;
import com.ishapirov.hotelapi.domainapi.RoomInformation;
import com.ishapirov.hotelapi.exceptions.*;

import com.ishapirov.hotelapi.formdata.BookRoom;
import com.ishapirov.hotelapi.formdata.adminreq.BookRoomForCustomer;
import com.ishapirov.hotelapi.formdata.CustomerCredentials;
import com.ishapirov.hotelapi.formdata.CustomerSignupInformation;
import com.ishapirov.hotelapi.token.TokenClass;
import com.ishapirov.hotelreservation.hotelclasses.Customer;
import com.ishapirov.hotelreservation.hotelclasses.Reservation;
import com.ishapirov.hotelreservation.hotelclasses.Room;
import com.ishapirov.hotelreservation.hotelclasses.RoomType;
import com.ishapirov.hotelreservation.repositories.CustomerRepository;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import com.ishapirov.hotelreservation.util.DomainToApiMapper;
import com.ishapirov.hotelreservation.util.HotelUtil;
import com.ishapirov.hotelreservation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private DomainToApiMapper domainToApiMapper;

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
    public CustomerInformation signup(CustomerSignupInformation customerSignupInformation) {
        Customer customer = new Customer(customerSignupInformation);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return domainToApiMapper.getCustomerInformation(customer);
    }

    @Override
    public TokenClass generateToken(CustomerCredentials customerCredentials) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    customerCredentials.getUsername(), customerCredentials.getPassword()));
        } catch (Exception ex) {
            throw new InvalidUsernameOrPasswordException("The username or password was incorrect");
        }
        return new TokenClass(jwtUtil.generateToken(customerCredentials.getUsername()));
    }

    @Override
    public CustomerInformation getCustomer(String username) {
        Optional<Customer> getCustomer = customerRepository.findByUsername(username);
        if(getCustomer.isEmpty())
            throw new CustomerNotFoundException("A customer with the given id was not found");
        return domainToApiMapper.getCustomerInformation(getCustomer.get());
    }

    @Override
    public RoomInformation getRoom(Integer roomNumber) {
        Optional<Room> getRoom = roomRepository.findByRoomNumber(roomNumber);
        if(getRoom.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
        return domainToApiMapper.getRoomInformation(getRoom.get());
    }

    @Override
    public List<RoomInformation> getRoomsByType(String roomType) {
        List<Room> rooms;
        if(roomType == null)
            rooms = roomRepository.findAll();
        else {
            Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
            if (getRoomType.isEmpty())
                throw new RoomTypeNotFoundException("A room type with the given room type name was not found");
            rooms = roomRepository.findByRoomType(getRoomType.get());
        }
        return domainToApiMapper.getRoomsInformation(rooms);
    }

    @Override
    public List<RoomInformation> getAvailableRooms(Date checkInDate,Date checkOutDate, String roomType){
        hotelUtil.validateDates(checkInDate, checkOutDate);
        List<Room> rooms;
        if(roomType == null){
            rooms = roomRepository.findAll();
        }
        else{
            Optional<RoomType> getRoomType = roomTypeRepository.findByName(roomType);
            if (getRoomType.isEmpty())
                throw new RoomTypeNotFoundException("A room type with the given room type name was not found");
            rooms = roomRepository.findByRoomType(getRoomType.get());
        }
        rooms = hotelUtil.returnRooms(rooms, checkInDate,checkOutDate);
        return domainToApiMapper.getRoomsInformation(rooms);
    }

    @Override
    public RoomInformation getRoomIfAvailable(Date checkInDate, Date checkOutDate, Integer roomNumber) {
        hotelUtil.validateDates(checkInDate,checkOutDate);
        Optional<Room> getRoom = roomRepository.findByRoomNumber(roomNumber);
        if(getRoom.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
        List<Reservation> reservations = reservationRepository.findByRoom(getRoom.get());
        if(!hotelUtil.isDateAvailable(reservations, checkInDate, checkOutDate))
            throw new ReservationOverlapException("The room has already been reserved for the given date interval");
        return domainToApiMapper.getRoomInformation(getRoom.get());
    }


    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReservationInformation bookRoomForCustomer(BookRoomForCustomer bookRoomForCustomer) {
        Optional<Customer> customer = customerRepository.findByUsername(bookRoomForCustomer.getUsername());
        Optional<Room> room = roomRepository.findByRoomNumber(bookRoomForCustomer.getRoomNumber());
        if(customer.isEmpty())
            throw new CustomerNotFoundException("A customer with the given id was not found");
        if(room.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
        hotelUtil.validateDates(bookRoomForCustomer.getCheckInDate(),bookRoomForCustomer.getCheckOutDate());
        List<Reservation> reservations = reservationRepository.findByRoom(room.get());
        if(!hotelUtil.isDateAvailable(reservations, bookRoomForCustomer.getCheckInDate(), bookRoomForCustomer.getCheckOutDate())){
            throw new ReservationOverlapException("The room has already been reserved for the given date interval");
        }
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setCheckInDate(bookRoomForCustomer.getCheckInDate());
        reservation.setCheckOutDate(bookRoomForCustomer.getCheckOutDate());
        reservationRepository.save(reservation);
        return domainToApiMapper.getReservationInformation(reservation);

    }

    @Override
    public ReservationInformation bookRoom(BookRoom bookRoom) {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        BookRoomForCustomer bookRoomForCustomer = new BookRoomForCustomer(username,bookRoom);
        return bookRoomForCustomer(bookRoomForCustomer);
    }

    @Override
    @Transactional
    public void cancelRoom(CancelReservation cancelReservation){
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(cancelReservation.getReservationNumber());
        if(reservation.isEmpty())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        if(reservationRepository.deleteByReservationNumber(cancelReservation.getReservationNumber()) != 1)
            throw new ReservationCreationError("Error while creating reservation");
        return;
    }

    @Override
    public ReservationInformation viewReservation(Integer reservationNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(!reservation.isPresent())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        Reservation userReservation = reservation.get();
        if(!userDetails.getUsername().equals(userReservation.getCustomer().getUsername()))
            throw new ReservationUsernamesDoNotMatchException("The username on the reservation does not match the username of the logged in user");
        return domainToApiMapper.getReservationInformation(userReservation);
    }

}
