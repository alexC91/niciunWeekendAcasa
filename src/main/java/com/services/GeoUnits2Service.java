package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.GeoUnits2Repository;

@Service
public class GeoUnits2Service {

    @Autowired
    private GeoUnits2Repository userRepository;
}

