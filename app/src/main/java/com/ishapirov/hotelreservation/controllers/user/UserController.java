package com.ishapirov.hotelreservation.controllers.user;

import com.ishapirov.hotelapi.generalexceptions.NotImplementedException;
import com.ishapirov.hotelapi.userservice.UserService;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
import com.ishapirov.hotelapi.userservice.exceptions.CustomerNotFoundException;
import com.ishapirov.hotelreservation.domain.Role;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.domain.UserSecurity;
import com.ishapirov.hotelreservation.repositories.RoleRepository;
import com.ishapirov.hotelreservation.repositories.UserRepository;
import com.ishapirov.hotelreservation.util.DomainToApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class UserController implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainToApiMapper domainToApiMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<UserInformation> getUsers() {
        throw new NotImplementedException("This operation is not yet supported");
    }

    @Override
    @Transactional
    public UserInformation newUser(UserSignupInformation userSignupInformation) {
        userSignupInformation.setPassword(passwordEncoder.encode(userSignupInformation.getPassword()));
        User user = new User(userSignupInformation);
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER"));
        UserSecurity userSecurity = new UserSecurity(userSignupInformation,roles);
        user.setUserSecurity(userSecurity);
        userRepository.save(user);
        return domainToApiMapper.getCustomerInformation(user);
    }

    @Override
    public UserInformation getUser(String username) {
        Optional<User> getUser = userRepository.findByUsername(username);
        if(getUser.isEmpty())
            throw new CustomerNotFoundException("A user with the given id was not found");
        return domainToApiMapper.getCustomerInformation(getUser.get());
    }

    @Override
    public UserInformation updateUser(String username, UserSignupInformation userInformation) {
        throw new NotImplementedException("This operation is not yet supported");
    }

    @Override
    public UserInformation deleteUser(String username) {
        throw new NotImplementedException("This operation is not yet supported");
    }
}
