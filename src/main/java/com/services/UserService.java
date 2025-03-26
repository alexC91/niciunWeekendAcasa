package com.services;

import com.linkDatabase.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void doMagic() {
        for (Users u : userRepository.findAll()) {
            System.out.println(u.getId());
        }
    }

    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        Users appUser = userRepository.findByEmail(email);

        if (appUser != null) {
            var springUser = User.withUsername(appUser.getEmail())
                    .password(appUser.getPassword())
                    .build();
        }

        return null;
    }
}
