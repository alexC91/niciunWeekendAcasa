package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) Which URLs are public vs. secured
                .authorizeHttpRequests(auth -> auth
                        // your public pages
                        .requestMatchers(
                                "/",
                                "/about",
                                "/blog",
                                "/services",
                                "/contact",
                                "/register",
                                "/register1",
                                "/login1",
                                "/counties",
                                "/cities",       // your test page
                                "/news",
                                "/map",
                                "/error/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/atractie",
                                "/fonts/**",
                                "flaticon/**",
                                "/scss/**",
                                "/favicon.ico"
                        ).permitAll()
                        // everything else requires authentication
                        .anyRequest().authenticated()
                )
                // 2) Form-login customization
                .formLogin(form -> form
                        // show login page on GET /login
                        .loginPage("/login")
                        // process credentials on POST /login (not /perform_login)
                        .loginProcessingUrl("/login")
                        // match your <input name="username"> and name="password"
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // where to go on success/failure
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        // allow everyone to see the login form & submit
                        .permitAll()
                )
                // 3) Logout configuration
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("jwt_token", "JSESSIONID")
                        .permitAll()
                )
                // 4) Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 5) Disable CSRF for API endpoints but keep it for form submissions
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**", "/login1", "/register1")
                );

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    public static class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws java.io.IOException {
            // Clear JWT cookie
            Cookie cookie = new Cookie("jwt_token", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            // Redirect to home page
            response.sendRedirect("/");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
