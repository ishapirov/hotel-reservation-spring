package com.ishapirov.hotelapi.testservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/services/test")
public interface TestService {
    @GetMapping
    Response test();

    @GetMapping("/token")
    String testToken();
}
