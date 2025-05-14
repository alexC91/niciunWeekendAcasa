package com.example.demo;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class Email {
    private static final String SMTP_SERVER = "smtp.gmail.com"; // Gmail SMTP Server
    private static final String USERNAME = "niciunweekendacasa1@gmail.com"; // Your Gmail Address
    private static final String PASSWORD = "vvhf dyxd cykx hpum"; // Gmail App Password
    private static final int SMTP_PORT = 587; // TLS Port
    private static final boolean EMAIL_ENABLED = true; // Set to false to disable email sending

    public static void sendEmail(String to, String subject, String body) {
        // If email is disabled, just log and return
        if (!EMAIL_ENABLED) {
            System.out.println("Email sending is disabled. Would have sent to: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.debug", "true"); // Enable debug mode
        props.put("mail.smtp.timeout", "10000"); // 10 seconds timeout
        props.put("mail.smtp.connectiontimeout", "10000"); // 10 seconds connection timeout

        try {
            // Create a Mail Session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // Enable session debugging
            session.setDebug(true);

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
                System.out.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
                throw e; // Re-throw to be caught by caller
            }
        } catch (Exception e) {
            System.out.println("Error in email configuration: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Test method to check if email configuration is working
    public static boolean testEmailConfiguration() {
        if (!EMAIL_ENABLED) {
            System.out.println("Email sending is disabled.");
            return false;
        }

        try {
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

            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_SERVER, USERNAME, PASSWORD);
            transport.close();

            System.out.println("Email configuration test successful");
            return true;
        } catch (Exception e) {
            System.out.println("Email configuration test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
