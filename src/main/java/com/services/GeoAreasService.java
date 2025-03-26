package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.GeoAreasRepository;

@Service
public class GeoAreasService {

    @Autowired
    private GeoAreasRepository userRepository;
}

