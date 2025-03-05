@ -0,0 +1,11 @@
        package com.turism.email;

public class Main {
    public static void main(String[] args) {
        System.out.println("Testare trimitere email...");
        EmailSender.sendEmail("destinatar@example.com", "Subiect Test", "Acesta este un e-mail de test.");

        System.out.println("Testare primire email...");
        EmailReceiver.receiveEmails();
    }
}