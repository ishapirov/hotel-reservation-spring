package com.ishapirov.hotelreservation.authentication;

import java.util.Optional;

import com.ishapirov.hotelreservation.repositories.UserSecurityRepository;
import com.ishapirov.hotelreservation.security.MyUserDetailsService;

import com.ishapirov.hotelreservation.domain.UserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private UserSecurityRepository userSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserSecurity> user = userSecurityRepository.findByUsername(username);
        user.orElseThrow(() -> 
            new UsernameNotFoundException(String.format("Username %s not found",username)));
        return user.map(MyUserDetailsService::new).get();
    }

}
