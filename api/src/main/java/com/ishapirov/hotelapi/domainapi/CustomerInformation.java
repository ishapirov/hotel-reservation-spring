package com.ishapirov.hotelapi.domainapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInformation {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
