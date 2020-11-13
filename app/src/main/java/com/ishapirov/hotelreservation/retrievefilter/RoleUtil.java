package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelreservation.domain.Role;
import com.ishapirov.hotelreservation.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleUtil {
    @Autowired
    private RoleRepository roleRepository;

    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER");
    }

    public Role getAdminRole(){
        return roleRepository.findByName("ROLE_ADMIN");
    }
}
