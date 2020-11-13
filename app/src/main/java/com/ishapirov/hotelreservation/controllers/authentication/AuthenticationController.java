package com.ishapirov.hotelreservation.controllers.authentication;

import com.ishapirov.hotelapi.services.authentication.AuthenticationService;
import com.ishapirov.hotelapi.services.authentication.credentials.UserCredentials;
import com.ishapirov.hotelapi.services.authentication.token.Token;
import com.ishapirov.hotelapi.services.authentication.exceptions.InvalidUsernameOrPasswordException;
import com.ishapirov.hotelreservation.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Token generateToken(UserCredentials userCredentials) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userCredentials.getUsername(), userCredentials.getPassword()));
        } catch (Exception ex) {
            throw new InvalidUsernameOrPasswordException("The username or password was incorrect");
        }
        return new Token(jwtUtil.generateToken(userCredentials.getUsername()));
    }
}
