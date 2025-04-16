package com.example.demo;

import com.linkDatabase.Users;
import com.repositories.UserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@RestController
public class NewAccountMail {

    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "niciunweekendacasa1@gmail.com";  // adresa ta Gmail
    private static final String PASSWORD = "vvhf dyxd cykx hpum";              // App Password pentru Gmail
    private static final int SMTP_PORT = 587;

    // Map local pentru stocarea token-urilor asociate email-urilor (doar pentru testare in memorie)
    private final Map<String, String> tokenToEmailMap = new HashMap<>();

    public String getTokenForEmail(String email) {
        for (Map.Entry<String, String> entry : tokenToEmailMap.entrySet()) {
            if (entry.getValue().equals(email)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint-ul de înregistrare.
     * – Verifică dacă utilizatorul există; dacă nu, îl creează cu isActivated = 0.
     * – Generează un token de verificare (stocat în memorie) și trimite un email cu link-ul de verificare.
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String email) {
        // 1. Caută user-ul după email; dacă nu există, îl creează
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            user = new Users();
            user.setFirstName(name);
            user.setEmail(email);
            user.setIsActivated((byte) 0);   // contul este inactiv până la verificare
            user.setIsDisabled((byte) 0);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }

        // 2. Generează un token de verificare și îl stochează în map
        String token = UUID.randomUUID().toString();
        tokenToEmailMap.put(token, email);

        // 3. Creează link-ul de verificare
        String verificationLink = "http://localhost:9090/verify?token=" + token;

        // 4. Construiește conținutul email-ului
        String subject = "Please verify your account";
        String body = "Hi " + name + ",\n\nClick the link below to verify your account:\n\n"
                + verificationLink + "\n\nThanks,\nThe niciunWeekendAcasa Team";

        // 5. Trimite emailul
        sendEmail(email, subject, body);

        return "Verification email sent to: " + email;
    }

    /**
     * Endpoint-ul de verificare.
     * – Verifică dacă token-ul primit există în map.
     * – Dacă da, caută utilizatorul prin email și setează isActivated la 1 (true).
     */
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token) {
        String email = tokenToEmailMap.get(token);
        if (email != null) {
            // Elimină token-ul din map, pentru a preveni reutilizarea lui
            tokenToEmailMap.remove(token);
            // Găsește utilizatorul în baza de date și actualizează starea de activare
            Users user = userRepository.findByEmail(email);
            if (user != null) {
                user.setIsActivated((byte) 1);
                userRepository.save(user);
                return "Account verified for email: " + email;
            } else {
                return "User not found for email: " + email;
            }
        } else {
            return "Invalid or expired verification link.";
        }
    }

    /**
     * Metoda pentru trimiterea emailurilor folosind SMTP Gmail.
     */
    private void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
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
            System.out.println("Email sent to: " + to);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
