package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.OriginsRepository;

@Service
public class OriginsService {

    @Autowired
    private OriginsRepository userRepository;
}

