@ -0,0 +1,39 @@
        package com.turism.email;

import jakarta.mail.*;
        import jakarta.mail.internet.*;

        import java.util.Properties;

public class EmailSender {
    private static final String USERNAME = "adresa_ta@gmail.com";
    private static final String PASSWORD = "parola_ta";

    public static void sendEmail(String to, String subject, String body) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("✅ Email trimis cu succes către: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}