@ -0,0 +1,53 @@
        package com.turism.email;

import jakarta.mail.*;
        import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

public class EmailReceiver {
    private static final String USERNAME = "adresa_ta@gmail.com";
    private static final String PASSWORD = "parola_ta";

    public static void receiveEmails() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getInstance(properties);
            Store store = session.getStore("imaps");
            store.connect(USERNAME, PASSWORD);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            System.out.println("📬 Ai " + messages.length + " mesaje în inbox.");
            for (int i = Math.max(0, messages.length - 5); i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Content: " + getTextFromMessage(message));
                System.out.println("--------------------------------------------------");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return mimeMultipart.getBodyPart(0).getContent().toString();
        }
        return "";
    }
}