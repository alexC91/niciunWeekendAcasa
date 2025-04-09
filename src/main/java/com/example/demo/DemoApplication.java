package com.example.demo;

import com.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.services",
		"com.example.demo",
		"com.linkDatabase",
		"com.repositories",
		"com.services"
})
@EnableJpaRepositories(basePackages = "com.repositories")
@EnableDiscoveryClient
@EntityScan(basePackages = "com.linkDatabase")
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoApplication.class);
		ConfigurableApplicationContext context = app.run(args);
		System.out.println("Application started");

		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(getConnection(new SimpleDriverDataSource()));
			for (Map<String, Object> s : jdbcTemplate.queryForList("SELECT 13")) {
				for (String x : s.keySet()) {
					System.out.println(x + " " + s.get(x));
				}
			}

			UserService someService = context.getBean(UserService.class);
			someService.doMagic();

            // Send a test email
            String recipient = "gigel@test.com"; // Replace with a real email
            String subject = "Test 123 Email from Spring Boot via Gmail SMTP";
            String body = "Hello 123! This is a test email sent using Gmail SMTP.";

            if (false) Email.sendEmail(recipient, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SimpleDriverDataSource getConnection(SimpleDriverDataSource dataSource) throws ClassNotFoundException {
		dataSource.setDriverClass(com.microsoft.sqlserver.jdbc.SQLServerDriver.class);
		//A se scimba  "jdbc:sqlserver://localhost:1433;  cu "jdbc:sqlserver://localhost:52122;
		dataSource.setUrl("jdbc:sqlserver://localhost:1433; databaseName=niciunWeekendAcasa;trustServerCertificate=false;encrypt=false");
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		return dataSource;
	}
}
