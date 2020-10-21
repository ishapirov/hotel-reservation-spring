package com.ishapirov.hotelapi.userservice;

import com.ishapirov.hotelapi.userservice.domain.UserInformation;
import com.ishapirov.hotelapi.userservice.domain.UserSignupInformation;
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
     * @PreAuthorize("hasRole('ROLE_ADMIN')")
     */
    @GetMapping
    Page<UserInformation> getUsers();

    @PostMapping
    UserInformation newUser(@RequestBody @Valid UserSignupInformation userSignupInformation);

    @GetMapping("/{username}")
    UserInformation getUser(@PathVariable @NotNull @Size(min=5,max=32) String username);

    @PutMapping("/{username}")
    UserInformation updateUser(@PathVariable @Size(min=5,max=32) String username,@RequestBody @Valid UserSignupInformation userInformation);

    @DeleteMapping("/{username}")
    UserInformation deleteUser(@PathVariable @Size(min=5,max=32) String username);

}
