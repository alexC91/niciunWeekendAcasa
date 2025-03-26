package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.StatusesRepository;

@Service
public class StatusesService {

    @Autowired
    private StatusesRepository userRepository;
}

