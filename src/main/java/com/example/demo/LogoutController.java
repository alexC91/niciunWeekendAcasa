package com.example.demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/manual-logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Get the current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // Perform logout
            new SecurityContextLogoutHandler().logout(request, response, auth);

            // Clear JWT cookie
            Cookie cookie = new Cookie("jwt_token", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        // Redirect to home page
        return "redirect:/";
    }
}
