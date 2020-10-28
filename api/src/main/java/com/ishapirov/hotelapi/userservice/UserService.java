package com.ishapirov.hotelapi.userservice;

import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
import com.ishapirov.hotelapi.userservice.exceptions.CustomerNotFoundException;
import com.ishapirov.hotelapi.userservice.paramvalidation.UsersCriteria;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Validated
@RequestMapping("/services/users")
public interface UserService {

    /**
     * Admin role required
     */
    @GetMapping
    HotelPage<UserInformation> getUsers(@Valid UsersCriteria usersCriteria);

    @PostMapping
    UserInformation newUser(@RequestBody @Valid UserSignupInformation userSignupInformation);

    @GetMapping("/{username}")
    UserInformation getUser(@PathVariable @NotNull @Size(min=5,max=32) String username)
            throws CustomerNotFoundException;

    @PutMapping("/{username}")
    UserInformation updateUser(@PathVariable @Size(min=5,max=32) String username,@RequestBody @Valid UserSignupInformation userInformation);

    @DeleteMapping("/{username}")
    UserInformation deleteUser(@PathVariable @Size(min=5,max=32) String username);

    /**
     * Admin role required
     */
    @PostMapping("/admin")
    UserInformation newAdmin(@RequestBody @Valid UserSignupInformation userSignupInformation);

}
