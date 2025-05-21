package com.example.demo;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class Email {
    private static final String SMTP_SERVER = "smtp.gmail.com"; // Gmail SMTP Server
    private static final String USERNAME = "niciunweekendacasa1@gmail.com"; // Your Gmail Address
    private static final String PASSWORD = "vvhf dyxd cykx hpum"; // Gmail App Password
    private static final int SMTP_PORT = 587; // TLS Port

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
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    // Method to test email configuration
    public boolean testEmailConfiguration() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_SERVER);
            props.put("mail.smtp.port", SMTP_PORT);

            // Create a Mail Session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // Test connection to the SMTP server
            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_SERVER, USERNAME, PASSWORD);
            transport.close();

            System.out.println("Email configuration test successful");
            return true;
        } catch (Exception e) {
            System.err.println("Email configuration test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // For backward compatibility - static method that uses the singleton instance
    // This helps maintain compatibility with existing code that uses the static method
    public static void sendStaticEmail(String to, String subject, String body) {
        // Get the Email bean from the Spring context
        Email emailService = SpringContextUtil.getBean(Email.class);
        emailService.sendEmail(to, subject, body);
    }
}
