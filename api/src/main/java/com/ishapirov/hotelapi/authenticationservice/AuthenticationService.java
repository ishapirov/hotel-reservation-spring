package com.ishapirov.hotelapi.authenticationservice;

import com.ishapirov.hotelapi.authenticationservice.exceptions.InvalidUsernameOrPasswordException;
import com.ishapirov.hotelapi.authenticationservice.token.Token;
import com.ishapirov.hotelapi.authenticationservice.credentials.UserCredentials;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/services/authentication")
public interface AuthenticationService {
    @PostMapping
    Token generateToken(@RequestBody @Valid UserCredentials userCredentials)
            throws InvalidUsernameOrPasswordException;
}
