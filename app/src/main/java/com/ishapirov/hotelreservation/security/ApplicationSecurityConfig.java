package com.ishapirov.hotelreservation.security;

import com.ishapirov.hotelreservation.authentication.ApplicationUserService;
import com.ishapirov.hotelreservation.filter.JwtFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    @Autowired
    private JwtFilter filter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/services/authentication").permitAll()

                .antMatchers(HttpMethod.POST,"/services/users").permitAll()
                .antMatchers(HttpMethod.GET,"/services/users/*").authenticated()
                .antMatchers(HttpMethod.PUT,"/services/users/*").authenticated()
                .antMatchers(HttpMethod.DELETE,"/services/users/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/services/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/services/users/admin").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET,"/services/rooms").permitAll()
                .antMatchers(HttpMethod.GET,"/services/rooms/*").permitAll()
                .antMatchers(HttpMethod.PUT,"/services/rooms/*").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET,"/services/reservations").authenticated()
                .antMatchers(HttpMethod.GET,"/services/reservations/*").authenticated()
                .antMatchers(HttpMethod.POST,"/services/reservations").authenticated()
                .antMatchers(HttpMethod.PUT,"/services/reservations/*").authenticated()
                .antMatchers(HttpMethod.DELETE,"/services/reservations/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/services/reservations/cancel").authenticated()
                .antMatchers(HttpMethod.GET,"/services/reservations/response/*").authenticated()
                .antMatchers(HttpMethod.POST,"/services/reservations/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/services/reservations/admin/cancel").hasRole("ADMIN")

                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }


}