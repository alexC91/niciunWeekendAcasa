package com.example.demo;

import com.linkDatabase.Comment;
import com.linkDatabase.Post;
import com.linkDatabase.Users;
import com.repositories.UserRepository;
import com.services.CommentService;
import com.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            model.addAttribute("user", user);

            // Get user's posts with comments
            List<Post> posts = postService.getUserPosts(user);
            for (Post post : posts) {
                List<Comment> comments = commentService.getPostComments(post);
                post.setComments(comments);
            }
            model.addAttribute("posts", posts);
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
            @RequestParam(required = false) String coverPhotoBase64,
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
                        // Extract the base64 data and content type
                        String[] parts = profilePhotoBase64.split(",");
                        if (parts.length < 2) {
                            throw new IllegalArgumentException("Invalid Base64 image format");
                        }

                        // Extract content type from the data URL
                        String contentType = "image/jpeg"; // Default
                        if (parts[0].contains("image/")) {
                            contentType = parts[0].substring(parts[0].indexOf("image/"), parts[0].indexOf(";"));
                        }

                        String base64Image = parts[1];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                        // Check file size (limit to 5MB)
                        if (imageBytes.length > 5 * 1024 * 1024) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("message", "Profile image is too large. Maximum size is 5MB.");
                            return "redirect:/profile_edit";
                        }

                        // Store the image in the database
                        user.setProfilePhotoData(imageBytes);
                        user.setProfilePhotoContentType(contentType);

                        // Set a unique identifier for the profile photo URL
                        String photoId = UUID.randomUUID().toString();
                        user.setProfilePhoto("/profile-photo/" + user.getId() + "?v=" + photoId);

                        System.out.println("Profile photo saved to database. Size: " + imageBytes.length + " bytes");
                        System.out.println("Profile photo URL set to: " + user.getProfilePhoto());
                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("error", true);
                        redirectAttributes.addFlashAttribute("message", "Failed to process profile image: " + e.getMessage());
                        return "redirect:/profile_edit";
                    }
                }

                // Handle cover photo if provided
                if (coverPhotoBase64 != null && !coverPhotoBase64.isEmpty() && coverPhotoBase64.startsWith("data:image")) {
                    try {
                        // Extract the base64 data and content type
                        String[] parts = coverPhotoBase64.split(",");
                        if (parts.length < 2) {
                            throw new IllegalArgumentException("Invalid Base64 image format");
                        }

                        // Extract content type from the data URL
                        String contentType = "image/jpeg"; // Default
                        if (parts[0].contains("image/")) {
                            contentType = parts[0].substring(parts[0].indexOf("image/"), parts[0].indexOf(";"));
                        }

                        String base64Image = parts[1];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                        // Check file size (limit to 5MB)
                        if (imageBytes.length > 5 * 1024 * 1024) {
                            redirectAttributes.addFlashAttribute("error", true);
                            redirectAttributes.addFlashAttribute("message", "Cover image is too large. Maximum size is 5MB.");
                            return "redirect:/profile_edit";
                        }

                        // Store the image in the database
                        user.setCoverPhotoData(imageBytes);
                        user.setCoverPhotoContentType(contentType);

                        // Set a unique identifier for the cover photo URL
                        String photoId = UUID.randomUUID().toString();
                        user.setCoverPhoto("/cover-photo/" + user.getId() + "?v=" + photoId);

                        System.out.println("Cover photo saved to database. Size: " + imageBytes.length + " bytes");
                        System.out.println("Cover photo URL set to: " + user.getCoverPhoto());
                    } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("error", true);
                        redirectAttributes.addFlashAttribute("message", "Failed to process cover image: " + e.getMessage());
                        return "redirect:/profile_edit";
                    }
                }

                userRepository.save(user);

                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Failed to update profile: " + e.getMessage());
        }

        return "redirect:/profile_edit";
    }

    @GetMapping("/profile-photo/{userId}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Integer userId, @RequestParam(required = false) String v) {
        Optional<Users> userOptional = userRepository.findById(Long.valueOf(userId));

        if (userOptional.isPresent() && userOptional.get().getProfilePhotoData() != null) {
            Users user = userOptional.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    user.getProfilePhotoContentType() != null ?
                            user.getProfilePhotoContentType() : "image/jpeg"
            ));

            return new ResponseEntity<>(user.getProfilePhotoData(), headers, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
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

                // Store the image in the database
                user.setProfilePhotoData(file.getBytes());
                user.setProfilePhotoContentType(file.getContentType());

                // Set a unique identifier for the profile photo URL
                String photoId = UUID.randomUUID().toString();
                user.setProfilePhoto("/profile-photo/" + user.getId() + "?v=" + photoId);

                userRepository.save(user);

                return "{\"success\":true,\"path\":\"" + user.getProfilePhoto() + "\"}";
            }

            return "{\"success\":false,\"message\":\"User not found or file is empty\"}";
        } catch (IOException e) {
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/cover-photo/{userId}")
    public ResponseEntity<byte[]> getCoverPhoto(@PathVariable Integer userId, @RequestParam(required = false) String v) {
        Optional<Users> userOptional = userRepository.findById(Long.valueOf(userId));

        if (userOptional.isPresent() && userOptional.get().getCoverPhotoData() != null) {
            Users user = userOptional.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    user.getCoverPhotoContentType() != null ?
                            user.getCoverPhotoContentType() : "image/jpeg"
            ));

            return new ResponseEntity<>(user.getCoverPhotoData(), headers, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }
}
