package com.ishapirov.hotelreservation.controllers.user;

import com.ishapirov.hotelapi.generalexceptions.NotImplementedException;
import com.ishapirov.hotelapi.generalexceptions.UnauthorizedAccessException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.userservice.UserService;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
import com.ishapirov.hotelapi.userservice.exceptions.CustomerNotFoundException;
import com.ishapirov.hotelapi.userservice.paramvalidation.UsersCriteria;
import com.ishapirov.hotelreservation.domain.Role;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.domain.UserSecurity;
import com.ishapirov.hotelreservation.repositories.RoleRepository;
import com.ishapirov.hotelreservation.repositories.UserRepository;
import com.ishapirov.hotelreservation.util.DomainToApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
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
    public HotelPage<UserInformation> getUsers(UsersCriteria usersCriteria) {
        Pageable pageable = PageRequest.of(usersCriteria.getPageNumber(), usersCriteria.getSize());
        return domainToApiMapper.getUserInformationPage(userRepository.findAll(pageable),pageable);
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
        return domainToApiMapper.getUserInformation(user);
    }

    @Override
    public UserInformation getUser(String username) {
        Optional<User> getUser = userRepository.findByUsername(username);
        if(getUser.isEmpty())
            throw new CustomerNotFoundException("A user with the given id was not found");
        User user = getUser.get();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getUsername() != userDetails.getUsername()){
            boolean hasAdminRole = userDetails.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
            if(!hasAdminRole)
                throw new UnauthorizedAccessException("The role admin is required to view others user information");
        }
        return domainToApiMapper.getUserInformation(user);
    }

    @Override
    public UserInformation updateUser(String username, UserSignupInformation userInformation) {
        throw new NotImplementedException("This operation is not yet supported");
    }

    @Override
    public UserInformation deleteUser(String username) {
        throw new NotImplementedException("This operation is not yet supported");
    }

    @Override
    public UserInformation newAdmin(@Valid UserSignupInformation userSignupInformation) {
        userSignupInformation.setPassword(passwordEncoder.encode(userSignupInformation.getPassword()));
        User user = new User(userSignupInformation);
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_ADMIN"));
        UserSecurity userSecurity = new UserSecurity(userSignupInformation,roles);
        user.setUserSecurity(userSecurity);
        userRepository.save(user);
        return domainToApiMapper.getUserInformation(user);
    }
}
