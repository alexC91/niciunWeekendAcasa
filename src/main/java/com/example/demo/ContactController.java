package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("activePage", "contact");
        return "contact"; // contact.html
    }

    @PostMapping("/sendContactEmail")
    public String sendContactEmail(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            Model model) {

        try {
            // Send email using the Email class
            Email.sendEmail("niciunweekendacasa1@yahoo.com", "Mesaj nou: " + subject,
                    "Ai primit un mesaj nou de la: " + name + " (" + email + ")\n\n" + message);

            model.addAttribute("success", true);
            model.addAttribute("message", "Mesajul tău a fost trimis cu succes!");
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "A apărut o eroare la trimiterea mesajului: " + e.getMessage());
        }

        model.addAttribute("activePage", "contact");
        return "contact";
    }
}
