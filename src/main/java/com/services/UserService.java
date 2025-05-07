package com.services;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public void doMagic() {
        for (Users u : userRepository.findAll()) {
            System.out.println("Magic user ID: " + u.getId());
        }
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