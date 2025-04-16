package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String showContactForm() {
        return "contact"; // contact.html
    }

    @PostMapping("/sendContactEmail")
    public String sendContactEmail(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String subject,
                                   @RequestParam String message) {
        String to = "niciunweekendacasa1@gmail.com"; // înlocuiește cu adresa ta de contact
        String subject_email = "Mesaj nou de la formularul de contact";
        String body = "Ai primit un mesaj nou de la: " + name + " (" + email + ")\n\n" + message;

        Email.sendEmail(to, subject_email, body);

        return "redirect:/contact?success"; // poți afișa un mesaj de confirmare în HTML
    }
}