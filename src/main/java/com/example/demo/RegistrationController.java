package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register1")
    public String registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        Users newUser = new Users();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setIsActivated(true);
        newUser.setIsDisabled(false);
        newUser.setCreatedAt(new Date());

        userRepository.save(newUser);
        return "redirect:/login";
    }

    @GetMapping("/login1")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password) {
        Users user = userRepository.findByEmail(email).get();
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // User is authenticated
            return "redirect:/";
        } else {
            // Authentication failed
            return "redirect:/login?error";
        }
    }
}