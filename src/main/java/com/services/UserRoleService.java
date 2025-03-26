package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.UserRoleRepository;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRepository;
}

