package com.ishapirov.hotelapi.services.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation {

    private Integer userID;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

}
