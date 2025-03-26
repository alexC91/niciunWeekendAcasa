package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.EthnoRegRepository;

@Service
public class EthnoRegService {

    @Autowired
    private EthnoRegRepository userRepository;
}
