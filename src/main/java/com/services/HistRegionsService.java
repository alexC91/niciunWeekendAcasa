package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.HistRegionsRepository;

@Service
public class HistRegionsService {

    @Autowired
    private HistRegionsRepository userRepository;
}

