package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Create directories for profile images
        String profileImagesDir = "src/main/resources/static/images/profiles";
        File directory = new File(profileImagesDir);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Created directory: " + profileImagesDir);
        }
    }
}
