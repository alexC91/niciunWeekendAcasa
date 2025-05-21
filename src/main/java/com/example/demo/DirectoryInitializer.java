package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DirectoryInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Create directories for profile images
        String profileImagesDir = "src/main/resources/static/images/profiles";
        File directory = new File(profileImagesDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created directory: " + profileImagesDir);
            } else {
                System.err.println("Failed to create directory: " + profileImagesDir);

                // Try to create the directory using Files API
                try {
                    Path path = Paths.get(profileImagesDir);
                    Files.createDirectories(path);
                    System.out.println("Created directory using Files API: " + profileImagesDir);
                } catch (Exception e) {
                    System.err.println("Failed to create directory using Files API: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Directory already exists: " + profileImagesDir);

            // Check if the directory is writable
            if (!directory.canWrite()) {
                System.err.println("WARNING: Directory is not writable: " + profileImagesDir);
            } else {
                System.out.println("Directory is writable: " + profileImagesDir);
            }
        }

        // Print the absolute path for debugging
        System.out.println("Absolute path: " + new File(profileImagesDir).getAbsolutePath());
    }
}
