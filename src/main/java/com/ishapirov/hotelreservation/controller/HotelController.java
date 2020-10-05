package com.ishapirov.hotelreservation.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ishapirov.hotelreservation.authentication.CustomerCredentials;
import com.ishapirov.hotelreservation.controller_objects.AvailableRoom;
import com.ishapirov.hotelreservation.controller_objects.AvailableRoomNoType;
import com.ishapirov.hotelreservation.controller_objects.BookRoom;
import com.ishapirov.hotelreservation.controller_objects.BookRoomForCustomer;
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

    @GetMapping("test")
    public String test() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();    
        return userDetails.getUsername() + userDetails.getAuthorities();
    }
    
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("signup")
    public Response signup(@RequestBody Customer customer){
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);
            return new Response("Sign in successful");
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody CustomerCredentials customerCredentials) throws Exception{
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(customerCredentials.getUsername(), customerCredentials.getPassword())
            );
        }
        catch (Exception ex){
            throw new Exception("invalid username or password");
        }
        return jwtUtil.generateToken(customerCredentials.getUsername());
    }

    @GetMapping("/getallrooms")
    public List<Room> getAllRooms(){    
        return roomRepository.findAll();
    }

    @GetMapping("/getroomsbytype")
    public List<Room> getRoomsByType(@RequestBody String roomTypeName){
        Optional<RoomType> roomType = roomTypeRepository.findByName(roomTypeName);
        if(roomType.isPresent())
            return roomRepository.findByRoomType(roomType.get());
        else
            return null;
    }

    // Queries don't work yet
    // @GetMapping("/getavailableroomsbytype")
    // public List<Room> getAvailableRoomsByType(@RequestBody AvailableRoom availableRoom){
    //     return roomRepository.findAllAvailableType(availableRoom.getRoomtypeName(), availableRoom.getCheckinDate(), availableRoom.getCheckoutDate());
    // }

    // @GetMapping("/getavailablerooms")
    // public List<Room> getAvailableRooms(@RequestBody AvailableRoomNoType availableRoom){
    //     return roomRepository.findAllAvailableNoType(availableRoom.getCheckinDate(), availableRoom.getCheckoutDate());
    // }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("bookRoomForCustomer")
    public Response bookRoomForCustomer(@RequestBody BookRoomForCustomer bookRoomForCustomer){
        Optional<Customer> customer = customerRepository.findByUsername(bookRoomForCustomer.getUsername());
        Optional<Room> room = roomRepository.findByRoomNumber(bookRoomForCustomer.getRoomNumber());
        if(!customer.isPresent())
            return new Response("Customer not found");
        if(!room.isPresent())
            return new Response("Room not found");
        // Query doesn't work yet
        // if(reservationRepository.isRoomAvailable(bookRoomForCustomer.getRoomNumber(), bookRoomForCustomer.getCheckInDate(), bookRoomForCustomer.getCheckOutDate()) != 0)
        //     return new Response("The Room has already been reserved for this time");
        
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setDateCheckIn(bookRoomForCustomer.getCheckInDate());
        reservation.setDateCheckOut(bookRoomForCustomer.getCheckOutDate());
        reservationRepository.save(reservation);
        return new Response("Customer has been successfully booked");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("bookRoom")
    public Response bookRoom(@RequestBody BookRoom bookRoom){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        BookRoomForCustomer bookRoomForCustomer = new BookRoomForCustomer(username,bookRoom);
        return bookRoomForCustomer(bookRoomForCustomer);
    }


 
}