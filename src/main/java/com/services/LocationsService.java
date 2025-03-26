package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.LocationsRepository;

@Service
public class LocationsService {

    @Autowired
    private LocationsRepository userRepository;
}

