package com.com.email.test;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSender {
    private static final String USERNAME = "niciunWeekendAcasa1@gmail.com"; // Email-ul tău
    private static final String PASSWORD = "niciunWeekendAcasa123"; // App Password

    public static void sendEmail(String to, String subject, String body) {
        // Configurare proprietăți SMTP pentru Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS activat
        props.put("mail.smtp.host", "smtp.gmail.com"); // Server SMTP Gmail
        props.put("mail.smtp.port", "587"); // Port TLS

        // Creare sesiune autentificată
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Creare mesaj email
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME, "TurismApp"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to, "Destinatar"));
            msg.setSubject(subject);
            msg.setText(body);

            // Trimitere email
            Transport.send(msg);
            System.out.println("Email trimis cu succes către: " + to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}