package com.ishapirov.hotelapi.formdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCredentials {
    private String username;
    private String password;
}
