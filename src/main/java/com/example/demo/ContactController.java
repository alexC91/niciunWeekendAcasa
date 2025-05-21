package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    private Email emailService;

    @GetMapping("/contact")
    public String showContactForm() {
        return "contact"; // contact.html
    }

    @PostMapping("/sendContactEmail")
    public String sendContactEmail(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes) {

        try {
            // Send the message to the website owner
            emailService.sendEmail(
                    "niciunweekendacasa1@yahoo.com", // Website owner's email
                    "Contact Form Submission: " + subject,
                    "Name: " + name + "\nEmail: " + email + "\nSubject: " + subject + "\nMessage: " + message
            );

            // Send confirmation email to the user
            emailService.sendEmail(
                    email,
                    "Thank you for contacting us",
                    "Dear " + name + ",\n\nThank you for contacting us. We will contact you soon.\n\nBest regards,\nThe Team"
            );

            // Add success message
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Your message has been sent. We will contact you soon!");
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Failed to send message: " + e.getMessage());
        }

        return "redirect:/contact";
    }
}
