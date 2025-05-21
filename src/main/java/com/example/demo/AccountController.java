package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AccountController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private Email emailService;

    private final Map<String, String> tokenToEmailMap = new HashMap<>();

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute(new RegisterDto());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result
    ) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            result.addError(new FieldError("registerDto", "confirmPassword", "Parolele nu corespund"));
        }

        Users existingUser = repo.findByEmail(registerDto.getEmail());
        if (existingUser != null) {
            result.addError(new FieldError("registerDto", "email", "Emailul este deja folosit"));
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Users newUser = new Users();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setIsActivated((byte) 0);
            newUser.setIsDisabled((byte) 0);
            newUser.setPassword(encoder.encode(registerDto.getPassword()));

            repo.save(newUser);

            // Generează token de verificare
            String token = UUID.randomUUID().toString();
            tokenToEmailMap.put(token, newUser.getEmail());

            // Trimite email cu link de activare și salvează-l în DB
            String link = "http://localhost:9090/verify?token=" + token;
            String subject = "Activează-ți contul niciunWeekendAcasa";
            String body = "Salut " + newUser.getFirstName() + ",\n\n" +
                    "Pentru a-ți activa contul, accesează link-ul de mai jos:\n\n" +
                    link + "\n\nMulțumim!";

            emailService.sendEmail(newUser.getEmail(), subject, body);

            model.addAttribute("message", "Contul a fost creat. Verifică emailul pentru activare.");
            return "register";

        } catch (Exception ex) {
            result.addError(new FieldError("registerDto", "firstName", ex.getMessage()));
            return "register";
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        String email = tokenToEmailMap.remove(token);
        if (email == null) {
            model.addAttribute("message", "Link invalid sau expirat.");
            return "verify_result";
        }

        Users user = repo.findByEmail(email);
        if (user == null) {
            model.addAttribute("message", "Utilizatorul nu a fost găsit.");
            return "verify_result";
        }

        user.setIsActivated((byte) 1);
        repo.saveAndFlush(user);

        model.addAttribute("message", "Cont activat! Poți să te loghezi.");
        return "redirect:/login";
    }
}
