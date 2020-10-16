package com.ishapirov.hotelreservation.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.ishapirov.hotelreservation.domain.Role;
import com.ishapirov.hotelreservation.domain.UserSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetailsService implements UserDetails {

    private static final long serialVersionUID = 3202378890736699195L;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public MyUserDetailsService(String username, String password) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    public MyUserDetailsService(String username, String password, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    public MyUserDetailsService(UserSecurity userSecurity){
        this.username = userSecurity.getUsername();
        this.password = userSecurity.getPassword();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(Role role:userSecurity.getRoles())
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        this.grantedAuthorities = authorities;
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    
    
}
