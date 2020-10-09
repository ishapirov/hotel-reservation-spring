package com.ishapirov.hotelreservation.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ishapirov.hotelreservation.controller.classes.AvailableRoom;
import com.ishapirov.hotelreservation.controller.classes.AvailableRoomNoType;
import com.ishapirov.hotelreservation.controller.classes.BookRoom;
import com.ishapirov.hotelreservation.controller.classes.BookRoomForCustomer;
import com.ishapirov.hotelreservation.controller.classes.CancelReservation;
import com.ishapirov.hotelreservation.controller.classes.CustomerCredentials;
import com.ishapirov.hotelreservation.controller.classes.RoomTypeName;
import com.ishapirov.hotelreservation.controller.classes.TokenClass;
import com.ishapirov.hotelreservation.controller.exceptions.CustomerNotFoundException;
import com.ishapirov.hotelreservation.controller.exceptions.InvalidUsernameOrPasswordException;
import com.ishapirov.hotelreservation.controller.exceptions.ReservationNotFoundException;
import com.ishapirov.hotelreservation.controller.exceptions.ReservationOverlapException;
import com.ishapirov.hotelreservation.controller.exceptions.ReservationUsernamesDoNotMatchException;
import com.ishapirov.hotelreservation.controller.exceptions.RoomNotFoundException;
import com.ishapirov.hotelreservation.controller.exceptions.RoomTypeNotFoundException;
import com.ishapirov.hotelreservation.hotel_objects.Customer;
import com.ishapirov.hotelreservation.hotel_objects.Reservation;
import com.ishapirov.hotelreservation.hotel_objects.Room;
import com.ishapirov.hotelreservation.hotel_objects.RoomType;
import com.ishapirov.hotelreservation.repositories.CustomerRepository;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import com.ishapirov.hotelreservation.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelController {

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
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
   @PostMapping("/signup")
    public Response signup(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return new Response("Sign in successful");
    }

    @PostMapping("/authenticate")
    public TokenClass generateToken(@RequestBody CustomerCredentials customerCredentials) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    customerCredentials.getUsername(), customerCredentials.getPassword()));
        } catch (Exception ex) {
            throw new InvalidUsernameOrPasswordException();
        }
        return new TokenClass(jwtUtil.generateToken(customerCredentials.getUsername()));
    } 

    @GetMapping("/getallrooms")
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping("/getroom")
    public Room getRoom(@RequestParam(required = true) Integer roomnum) {
        Optional<Room> room = roomRepository.findByRoomNumber(roomnum);
        if(!room.isPresent())
            throw new RoomNotFoundException();
        return room.get();
    }

    @GetMapping("/getroomsbytype")
    public List<Room> getRoomsByType(@RequestBody RoomTypeName roomTypeName) {
        Optional<RoomType> roomType = roomTypeRepository.findByName(roomTypeName.getRoomTypeName());
        if (!roomType.isPresent())
            throw new RoomTypeNotFoundException();
        return roomRepository.findByRoomType(roomType.get());
    }

    @GetMapping("/getavailablerooms")
    public List<Room> getAvailableRooms(@RequestBody AvailableRoomNoType availableRoom){
        List<Room> rooms = roomRepository.findAll();
        return returnRooms(rooms, availableRoom.getCheckInDate(),availableRoom.getCheckOutDate());
    }

    @GetMapping("/getavailableroomsbytype")
    public List<Room> getAvailableRoomsByType(@RequestBody AvailableRoom availableRoom) {
        Optional<RoomType> roomType = roomTypeRepository.findByName(availableRoom.getRoomTypeName());
        if (!roomType.isPresent())
            throw new RoomTypeNotFoundException();
        List<Room> rooms = roomRepository.findByRoomType(roomType.get());
        return returnRooms(rooms, availableRoom.getCheckInDate(),availableRoom.getCheckOutDate());
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
