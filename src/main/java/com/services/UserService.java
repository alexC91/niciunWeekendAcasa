package com.services;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users appUser = userRepository.findByEmail(email);

        if (appUser == null) {
            log.warn("Login failed: user with email '{}' not found", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        if (appUser.getIsActivated() != (byte)1) {
            log.warn("Login failed for '{}': account not activated (isActivated={})",
                    email, appUser.getIsActivated());
            throw new DisabledException("Account is not activated");
        }

        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(/* eventuale roluri */)
                .build();
    }
}