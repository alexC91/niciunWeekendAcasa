package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/about")
    public String about() {
        return "about.html";
    }
    @GetMapping("/services")
    public String services() {
        return "services.html";
    }
    @GetMapping("/blog")
    public String blog() {
        return "blog.html";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact.html";
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
