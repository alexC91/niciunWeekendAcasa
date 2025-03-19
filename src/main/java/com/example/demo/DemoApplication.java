package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
//@EnableJpaRepositories
//@EnableDiscoveryClient
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoApplication.class);
		app.run(args);
		System.out.println("Application started");

		// Send a test email
		String recipient = "acostache0909@upb.ro"; // Replace with a real email
		String subject = "Test Email from Spring Boot via Gmail SMTP";
		String body = "Hello! This is a test email sent using Gmail SMTP.";

		Email.sendEmail(recipient, subject, body);
	}

}
