package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelapi.generalexceptions.UnauthorizedAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUtil {
    public String getUsernameOfCurrentUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public boolean isAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean usernameMatchesLoggedIn(String username){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return username.equals(userDetails.getUsername());
    }

    public void verifyActionAllowedForUser(String username){
        if(!isAdmin() && !usernameMatchesLoggedIn(username))
            throw new UnauthorizedAccessException("The role admin is required to view others user information");
    }
}
