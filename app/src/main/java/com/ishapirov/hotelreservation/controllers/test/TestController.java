package com.ishapirov.hotelreservation.controllers.test;

import com.ishapirov.hotelapi.testservice.Response;
import com.ishapirov.hotelapi.testservice.TestService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestService {
    @Override
    public Response test() {
        return new Response("Test successful!");
    }

    @Override
    public String testToken() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername() + userDetails.getAuthorities();
    }
}
