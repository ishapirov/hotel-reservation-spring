package com.ishapirov.hotelapi.services.authentication;

import com.ishapirov.hotelapi.services.authentication.exceptions.InvalidUsernameOrPasswordException;
import com.ishapirov.hotelapi.services.authentication.token.Token;
import com.ishapirov.hotelapi.services.authentication.credentials.UserCredentials;
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
