package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.*;
import jakarta.servlet.http.*;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller
public class RegistrationController {
    public String token;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Email emailService;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register1")
    public String registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response) {

        Users newUser = new Users();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setIsActivated(false); // Set to false to require activation
        newUser.setIsDisabled(false);
        newUser.setCreatedAt(new Date());

        // Generate activation token
        String activationToken = jwtUtil.generateToken(email);
        newUser.setResetToken(activationToken); // Using resetToken field for activation

        userRepository.save(newUser);

        // Send activation email
        try {
            String activationLink = "http://localhost:9090/activate?token=" + activationToken;

            emailService.sendEmail(
                    email,
                    "Activate Your Account",
                    "Thank you for registering with us. Please click the link below to activate your account:\n\n" +
                            activationLink + "\n\n" +
                            "If you did not register for an account, please ignore this email."
            );
        } catch (Exception e) {
            System.err.println("Failed to send activation email: " + e.getMessage());
        }

        return "redirect:/login?registered=true";
    }

    @GetMapping("/login1")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response) {

        Users user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Check if user is activated
            if (!user.getIsActivated()) {
                return "redirect:/login?error=not_activated";
            }

            // User is authenticated
            token = jwtUtil.generateToken(email);

            // Set JWT as cookie
            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 1 hour
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "redirect:/";
        } else {
            // Authentication failed
            return "redirect:/login?error";
        }
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("token") String token) {
        try {
            // Validate token
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractUsername(token);

                Optional<Users> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                    Users user = userOptional.get();

                    // Check if token matches stored token
                    if (token.equals(user.getResetToken())) {
                        user.setIsActivated(true);
                        user.setResetToken(null); // Clear the token
                        userRepository.save(user);

                        return "redirect:/login?activated=true";
                    }
                }
            }

            return "redirect:/login?error=invalid_token";
        } catch (Exception e) {
            return "redirect:/login?error=activation_failed";
        }
    }
}
