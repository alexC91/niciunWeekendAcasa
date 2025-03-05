package com.example.demo;

import com.com.email.test.EmailSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
		System.out.println("Application started");

		System.out.println("Trimitere email de test...");
		EmailSender.sendEmail("niciunWeekendAcasa1@gmail.com", "Test Email", "Acesta este un email de test din Java.");
	}
}
