package com.services;

import com.linkDatabase.Users;
import org.springframework.beans.factory.annotation.Autowired;
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
}
