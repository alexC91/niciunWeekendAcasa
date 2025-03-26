package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.GeoUnitsRepository;

@Service
public class GeoUnitsService {

    @Autowired
    private GeoUnitsRepository userRepository;
}

