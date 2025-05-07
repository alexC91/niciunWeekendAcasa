package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AccountController {

    @Autowired
    private UserRepository repo;

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "niciunweekendacasa1@gmail.com";
    private static final String PASSWORD = "vvhf dyxd cykx hpum";
    private static final int SMTP_PORT = 587;

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

            // Trimite email
            String link = "http://localhost:9090/verify?token=" + token;
            String subject = "Activează-ți contul niciunWeekendAcasa";
            String body = "Salut " + newUser.getFirstName() + ",\n\n" +
                    "Pentru a-ți activa contul, accesează link-ul de mai jos:\n\n" +
                    link + "\n\nMulțumim!";

            sendEmail(newUser.getEmail(), subject, body);

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
        return "redirect:/login";  // IMPORTANT: redirecționează spre pagina de login
    }

    private void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME, "niciunWeekendAcasa"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
