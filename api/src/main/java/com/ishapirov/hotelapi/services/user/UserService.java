package com.ishapirov.hotelapi.services.user;

import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.user.domain.UserInformation;
import com.ishapirov.hotelapi.services.user.domain.UserSignupInformation;
import com.ishapirov.hotelapi.services.user.domain.UserUpdate;
import com.ishapirov.hotelapi.services.user.exceptions.UserNotFoundException;
import com.ishapirov.hotelapi.services.user.paramvalidation.UsersCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
            throws UserNotFoundException;

    @PutMapping("/{username}")
    UserInformation updateUser(@PathVariable @Size(min=5,max=32) String username,@RequestBody @Valid UserUpdate userUpdate);

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable @Size(min=5,max=32) String username);

    /**
     * Admin role required
     */
    @PostMapping("/admin")
    UserInformation newAdmin(@RequestBody @Valid UserSignupInformation userSignupInformation);

}
