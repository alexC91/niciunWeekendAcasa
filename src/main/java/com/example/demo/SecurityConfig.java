package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

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
                                "/error/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"
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
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
