package com.ishapirov.hotelreservation.authentication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCredentials {
    private String username;
    private String password;
}
