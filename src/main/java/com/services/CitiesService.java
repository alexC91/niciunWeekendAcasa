package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.CitiesRepository;

@Service
public class CitiesService {

    @Autowired
    private CitiesRepository citiesRepository;
}
