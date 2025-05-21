package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ForgotPasswordController {

    private static final Map<String, String> resetTokenMap = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Users user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            model.addAttribute("error", "Nu există niciun cont cu acest email.");
            return "forgot_password";
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        resetTokenMap.put(resetToken, email);

        // Send password reset email
        String resetLink = "http://localhost:9090/reset-password?token=" + resetToken;
        String subject = "Resetare parolă niciunWeekendAcasa";
        String body = "Salut " + user.getFirstName() + ",\n\n" +
                "Pentru a-ți reseta parola, accesează link-ul de mai jos:\n\n" +
                resetLink + "\n\n" +
                "Dacă nu ai solicitat resetarea parolei, ignoră acest email.\n\n" +
                "Mulțumim!";

//        Email.sendEmail(email, subject, body);

        model.addAttribute("success", true);
        model.addAttribute("message", "Un email cu instrucțiuni pentru resetarea parolei a fost trimis.");
        return "forgot_password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        String email = resetTokenMap.get(token);

        if (email == null) {
            model.addAttribute("error", "Token invalid sau expirat.");
            return "error";
        }

        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        String email = resetTokenMap.get(token);

        if (email == null) {
            model.addAttribute("error", "Token invalid sau expirat.");
            return "error";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Parolele nu corespund.");
            model.addAttribute("token", token);
            return "reset_password";
        }

        Users user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            model.addAttribute("error", "Utilizatorul nu a fost găsit.");
            return "error";
        }

        // Update password
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // Remove token
        resetTokenMap.remove(token);

        model.addAttribute("success", true);
        model.addAttribute("message", "Parola a fost resetată cu succes. Te poți loga acum.");
        return "login";
    }
}
