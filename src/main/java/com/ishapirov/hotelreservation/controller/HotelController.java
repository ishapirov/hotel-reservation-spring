package com.ishapirov.hotelreservation.controller;

import javax.transaction.Transactional;

import com.ishapirov.hotelreservation.authentication.CustomerCredentials;
import com.ishapirov.hotelreservation.hotel_objects.Customer;
import com.ishapirov.hotelreservation.repositories.CustomerRepository;
import com.ishapirov.hotelreservation.repositories.DatesBookedRepository;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private DatesBookedRepository datesBookedRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("test")
    public String test() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getDetails();
        System.out.println(principal);
        return "test";
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

}
