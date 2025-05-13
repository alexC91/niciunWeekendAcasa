package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.*;
import java.util.Collections;
import java.util.Date;

@Controller
public class RegistrationController {
    public String token;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
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
                            @RequestParam String password,
                            HttpServletResponse response) {
        try {
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (passwordEncoder.matches(password, user.getPassword())) {
                // User is authenticated
                token = jwtUtil.generateToken(email);

                // Add token to response header
                response.addHeader("Authorization", "Bearer " + token);

                // Add token as cookie
                Cookie jwtCookie = new Cookie("jwt_token", token);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(3600); // 1 hour
                jwtCookie.setHttpOnly(true);
                response.addCookie(jwtCookie);

                // Set authentication in SecurityContextHolder
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return "redirect:/";
            } else {
                // Authentication failed
                return "redirect:/login?error=invalid_credentials";
            }
        } catch (Exception e) {
            return "redirect:/login?error=" + e.getMessage();
        }
    }
}
