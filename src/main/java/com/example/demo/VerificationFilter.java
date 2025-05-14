package com.example.demo;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class VerificationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        // Check if we need to show the activation notification
        if (session != null && session.getAttribute("accountNotActivated") != null) {
            // Add the notification attribute to the request so it can be accessed in the templates
            request.setAttribute("showActivationNotification", true);
            request.setAttribute("userEmail", session.getAttribute("userEmail"));
        }

        chain.doFilter(request, response);
    }
}
