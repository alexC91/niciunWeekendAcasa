package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.TypesRepository;

@Service
public class TypesService {

    @Autowired
    private TypesRepository userRepository;
}

