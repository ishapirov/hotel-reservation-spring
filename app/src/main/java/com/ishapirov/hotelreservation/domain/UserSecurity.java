package com.ishapirov.hotelreservation.domain;

import com.ishapirov.hotelapi.services.user.domain.UserSignupInformation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_security")
@Data
@NoArgsConstructor
public class UserSecurity {
    @Id
    private String username;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public UserSecurity(UserSignupInformation userSignupInformation, HashSet<Role> roles){
        this.username = userSignupInformation.getUsername();
        this.password = userSignupInformation.getPassword();
        this.roles = roles;
    }

}