package com.ishapirov.hotelreservation.authentication;

import java.util.Optional;

import com.ishapirov.hotelreservation.hotelclasses.Customer;
import com.ishapirov.hotelreservation.repositories.CustomerRepository;
import com.ishapirov.hotelreservation.security.SecurityUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    @Autowired
    private CustomerRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> 
            new UsernameNotFoundException(String.format("Username %s not found",username)));
        return user.map(SecurityUser::new).get();
    }

}
