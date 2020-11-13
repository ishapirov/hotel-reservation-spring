package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelreservation.domain.UserSecurity;
import com.ishapirov.hotelreservation.repositories.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecurityUtil {
    @Autowired
    private UserSecurityRepository userSecurityRepository;

    public UserSecurity getUserSecurity(String username){
        Optional<UserSecurity> userSecurity = userSecurityRepository.findByUsername(username);
        return userSecurity.get();
    }

}
