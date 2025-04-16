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
    public String sendContactEmail(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message) {

        // trimite mailul
        Email.sendEmail("niciunweekendacasa1@yahoo.com", "Mesaj nou: " + subject,
                "Ai primit un mesaj nou de la: " + name + " (" + email + ")\n\n" + message);

        return "redirect:/contact?success";
    }
}
