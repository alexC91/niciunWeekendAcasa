package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            model.addAttribute("user", user);
        }

        model.addAttribute("activePage", "profile");

        return "profile";
    }

    @GetMapping("/profile_about")
    public String showProfileAbout(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            model.addAttribute("user", user);
        }

        model.addAttribute("activePage", "profile_about");
        return "profile_about";
    }

    @GetMapping("/profile_photos")
    public String showProfilePhotos(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            model.addAttribute("user", user);
        }

        model.addAttribute("activePage", "profile_photos");
        return "profile_photos";
    }

    @GetMapping("/friends")
    public String showFriends(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            model.addAttribute("user", user);
        }

        model.addAttribute("activePage", "friends");
        return "friends";
    }

    @GetMapping("/profile_edit")
    public String showProfileEdit(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            model.addAttribute("user", user);
        }

        model.addAttribute("activePage", "profile_edit");
        return "profile_edit";
    }

    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String about,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String day,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String profilePhotoBase64,
            RedirectAttributes redirectAttributes) {

        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();

                // Update user fields if provided
                if (firstName != null && !firstName.trim().isEmpty()) {
                    user.setFirstName(firstName);
                }

                if (lastName != null && !lastName.trim().isEmpty()) {
                    user.setLastName(lastName);
                }

                // Update additional fields
                if (about != null) {
                    user.setAboutMe(about);
                }

                if (mobile != null) {
                    user.setMobile(mobile);
                }

                if (country != null) {
                    user.setCountry(country);
                }

                if (city != null) {
                    user.setCity(city);
                }

                if (state != null) {
                    user.setState(state);
                }

                if (gender != null) {
                    user.setGender(gender);
                }

                // Combine day, month, year into birthdate if all are provided
                if (day != null && month != null && year != null) {
                    String birthdate = day + "-" + month + "-" + year;
                    user.setBirthdate(birthdate);
                }

                if (language != null) {
                    user.setLanguage(language);
                }

                // Handle profile photo if provided
                if (profilePhotoBase64 != null && !profilePhotoBase64.isEmpty() && profilePhotoBase64.startsWith("data:image")) {
                    try {
                        // Extract the base64 data
                        String base64Image = profilePhotoBase64.split(",")[1];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                        // Check file size (limit to 5MB)
                        if (imageBytes.length > 5 * 1024 * 1024) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("message", "Profile image is too large. Maximum size is 5MB.");
                            return "redirect:/profile_edit";
                        }

                        // Generate a unique filename
                        String filename = UUID.randomUUID().toString() + ".jpg";
                        String uploadDir = "src/main/resources/static/images/profiles/";

                        // Create directory if it doesn't exist
                        File directory = new File(uploadDir);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }

                        // Save the file
                        Path path = Paths.get(uploadDir + filename);
                        Files.write(path, imageBytes);

                        // Update user profile photo path
                        user.setProfilePhoto("/images/profiles/" + filename);
                    } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("error", true);
                        redirectAttributes.addFlashAttribute("message", "Failed to process profile image: " + e.getMessage());
                        return "redirect:/profile_edit";
                    }
                }

                userRepository.save(user);

                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Failed to update profile: " + e.getMessage());
        }

        return "redirect:/profile_edit";
    }

    @PostMapping("/upload-profile-photo")
    @ResponseBody
    public String uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent() && !file.isEmpty()) {
                Users user = userOptional.get();

                // Generate a unique filename
                String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                String uploadDir = "src/main/resources/static/images/profiles/";

                // Create directory if it doesn't exist
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Save the file
                Path path = Paths.get(uploadDir + filename);
                Files.write(path, file.getBytes());

                // Update user profile photo path
                user.setProfilePhoto("/images/profiles/" + filename);
                userRepository.save(user);

                return "{\"success\":true,\"path\":\"/images/profiles/" + filename + "\"}";
            }

            return "{\"success\":false,\"message\":\"User not found or file is empty\"}";
        } catch (IOException e) {
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
