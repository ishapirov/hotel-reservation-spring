package com.ishapirov.hotelreservation.retrievefilter;

import com.ishapirov.hotelapi.generalexceptions.UnauthorizedAccessException;
import com.ishapirov.hotelapi.services.user.domain.UserInformation;
import com.ishapirov.hotelapi.services.user.domain.UserSignupInformation;
import com.ishapirov.hotelapi.services.user.domain.UserUpdate;
import com.ishapirov.hotelapi.services.user.exceptions.UserNotFoundException;
import com.ishapirov.hotelreservation.domain.Role;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.domain.UserSecurity;
import com.ishapirov.hotelreservation.repositories.UserRepository;
import com.ishapirov.hotelreservation.repositories.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserUtil {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSecurityUtil userSecurityUtil;
    @Autowired
    private RoleUtil roleUtil;
    @Autowired
    private AuthenticationUtil authenticationUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty())
            throw new UserNotFoundException("A user with the given id was not found");
        authenticationUtil.verifyActionAllowedForUser(username);
        return user.get();
    }

    public Page<User> getUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User createNewUser(UserSignupInformation userSignupInformation){
        userSignupInformation.setPassword(passwordEncoder.encode(userSignupInformation.getPassword()));
        User user = new User(userSignupInformation);
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleUtil.getUserRole());
        UserSecurity userSecurity = new UserSecurity(userSignupInformation,roles);
        user.setUserSecurity(userSecurity);
        userRepository.save(user);
        return user;
    }

    public User createNewAdmin(UserSignupInformation userSignupInformation){
        userSignupInformation.setPassword(passwordEncoder.encode(userSignupInformation.getPassword()));
        User user = new User(userSignupInformation);
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleUtil.getAdminRole());
        UserSecurity userSecurity = new UserSecurity(userSignupInformation,roles);
        user.setUserSecurity(userSecurity);
        userRepository.save(user);
        return user;
    }

    public User updateUser(String username, UserUpdate userUpdate){
        User user = getUserByUsername(username);
        user.setEmail(userUpdate.getEmail());
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        userRepository.save(user);
        return user;
    }

    public void confirmUserExists(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty())
            throw new UserNotFoundException("A user with the given id was not found");
    }

    public void deleteUser(String username){
        User user = getUserByUsername(username);
        userRepository.delete(user);
    }
}
