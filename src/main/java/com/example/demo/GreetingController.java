package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greet")
    public String greet() {
        return "Hey there!";
    }
    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
    @GetMapping("/register")
    public String register() {
        return "signup.html";
    }
}
