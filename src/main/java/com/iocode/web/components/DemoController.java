package com.iocode.web.components;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Welcome to Oauth2 Resource Server.!";
    }

    @GetMapping("/persons")
    public List<Person> demo() {
        return List.of(new Person("John Doe", 30, "johndoe@example.com"));
    }

}