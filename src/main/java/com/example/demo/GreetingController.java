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
}
