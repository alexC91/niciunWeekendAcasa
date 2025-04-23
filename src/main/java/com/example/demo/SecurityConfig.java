package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                                "/cities",       // your test page
                                "/error/**"
                        ).permitAll()
                        // static assets
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // everything else requires authentication
                        .anyRequest().authenticated()
                )
                // 2) Form-login customization
                .formLogin(form -> form
                        // show login page on GET /login
                        .loginPage("/login")
                        // process credentials on POST /perform_login
                        .loginProcessingUrl("/perform_login")
                        // match your <input name="username"> and name="password"
                        .usernameParameter("username")
                        .passwordParameter("password")
                        // where to go on success/failure
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        // allow everyone to see the login form & submit
                        .permitAll()
                )
                // 3) Logout
                .logout(logout -> logout
                        // if you want a custom logout URL:
                        // .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
        // 4) (Optional) leave CSRF protection on,
        //    just be sure to include the token in all your <form>s:
        //    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
