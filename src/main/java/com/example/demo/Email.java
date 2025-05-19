package com.example.demo;

import com.repositories.SentEmailRepository;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.time.LocalDateTime;


@Service
public class Email {
    private static final String SMTP_SERVER = "smtp.gmail.com"; // Gmail SMTP Server
    private static final String USERNAME = "niciunweekendacasa1@gmail.com"; // Your Gmail Address
    private static final String PASSWORD = "vvhf dyxd cykx hpum"; // Gmail App Password
    private static final int SMTP_PORT = 587; // TLS Port

    @Autowired
    private SentEmailRepository sentEmailRepository;

    public void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.smtp.port", SMTP_PORT);

        // Create a Mail Session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create Email Message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME, "niciunWeekendAcasa"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(body);

            // Send Email
            Transport.send(msg);
            System.out.println("Email sent successfully to: " + to);

            // Salvare în baza de date
            SentEmail email = new SentEmail();
            email.setRecipient(to);
            email.setSubject(subject);
            email.setContent(body);
            email.setSentAt(LocalDateTime.now());
            sentEmailRepository.save(email);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}