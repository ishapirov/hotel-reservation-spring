package com.ishapirov.hotelapi.services.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {
    private String email;
    private String firstName;
    private String lastName;
}
