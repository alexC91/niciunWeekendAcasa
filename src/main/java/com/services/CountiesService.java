package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.CountiesRepository;

@Service
public class CountiesService {

    @Autowired
    private CountiesRepository userRepository;
}
