package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.RolesRepository;

@Service
public class RolesService {

    @Autowired
    private RolesRepository userRepository;
}

