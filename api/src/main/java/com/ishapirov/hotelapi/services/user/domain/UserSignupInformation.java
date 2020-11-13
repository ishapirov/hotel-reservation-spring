package com.ishapirov.hotelapi.services.user.domain;

import com.ishapirov.hotelapi.services.user.constraints.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupInformation {
        @NotNull
        @Size(min=5,max=32)
        private String username;

        @ValidPassword
        private String password;

        @NotNull
        @Email
        private String email;

        @NotNull
        @Size(max=64)
        private String firstName;

        @NotNull
        @Size(max=64)
        private String lastName;
}
