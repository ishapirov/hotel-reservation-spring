package com.ishapirov.hotelapi.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {
    private String username;
    private String email;
    private String firstName;
    private String lastName;

}
