package com.ishapirov.hotelapi.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupInformation {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
}
