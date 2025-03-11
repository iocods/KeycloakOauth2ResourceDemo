package com.iocode.web.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableMethodSecurity
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Welcome to Oauth2 Resource Server.!";
    }

    @GetMapping("/persons")
    @PreAuthorize("hasRole('USER')")
    public List<Person> demo(Authentication authentication) {
        log.info("Authentication Authorities: {}", authentication.getAuthorities());
        log.info("Authentication Principal: {}", authentication.getPrincipal().toString());
        log.info("Authentication Details: {}", authentication.getDetails().toString());
        return List.of(new Person("John Doe", 30, "johndoe@example.com"));
    }

}