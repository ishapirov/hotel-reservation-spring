package com.ishapirov.hotelapi.services.authentication.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {
    @NotNull
    @Size(min=5,max=32)
    private String username;

    @NotNull
    private String password;
}
