package com.ishapirov.hotelapi.userservice;

import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services/users")
public interface UserService {

    /**
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @GetMapping
    List<UserInformation> getUsers();

    @PostMapping
    UserInformation newUser(@RequestBody UserSignupInformation userSignupInformation);

    @GetMapping("/{username}")
    UserInformation getUser(@PathVariable String username);

}
