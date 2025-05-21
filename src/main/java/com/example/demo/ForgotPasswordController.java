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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Email emailService;

    @GetMapping("/mailresetpassword")
    public String showMailResetPasswordForm(Model model) {
        model.addAttribute("activePage", "mailresetpassword");
        return "mailresetpassword";
    }

    @PostMapping("/mailresetpassword")
    public String processResetPasswordRequest(@RequestParam("email") String email, Model model) {
        Optional<Users> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            userRepository.save(user);

            String resetUrl = "http://localhost:9090/resetpassword?token=" + token;

            try {
                emailService.sendEmail(
                        email,
                        "Password Reset Request",
                        "To reset your password, click the link below:\n" + resetUrl
                );
                model.addAttribute("success", true);
                model.addAttribute("message", "If an account exists with that email, a reset link has been sent.");
            } catch (Exception e) {
                model.addAttribute("error", "Error sending email: " + e.getMessage());
            }
        } else {
            // For security reasons, don't reveal that the email doesn't exist
            model.addAttribute("success", true);
            model.addAttribute("message", "If an account exists with that email, a reset link has been sent.");
        }

        return "mailresetpassword";
    }

    @GetMapping("/resetpassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<Users> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isPresent()) {
            model.addAttribute("token", token);
            model.addAttribute("activePage", "resetpassword");
            return "resetpassword";
        } else {
            model.addAttribute("error", "Invalid or expired token");
            return "mailresetpassword";
        }
    }

    @PostMapping("/resetpassword")
    public String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model, RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "resetpassword";
        }

        Optional<Users> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Your password has been reset successfully. You can now login with your new password.");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Invalid or expired token");
            return "mailresetpassword";
        }
    }
}
