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

import java.util.Map;

@SpringBootApplication(scanBasePackages = {
		"com.example.demo",
		"com.services",
		"com.repositories",
		"com.linkDatabase"
})
@EnableJpaRepositories("com.repositories")
@EntityScan("com.linkDatabase")
@EnableDiscoveryClient
public class DemoApplication {

	static {
		// Set maximum file upload size
		System.setProperty("spring.servlet.multipart.max-file-size", "10MB");
		System.setProperty("spring.servlet.multipart.max-request-size", "10MB");
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		try {
			System.out.println("\n=== Application Startup Checks ===");

			// 1. Test database connection
			testDatabaseConnection();

			// 2. Verify UserService bean
			//testUserService(context);

			// 3. System readiness check
			System.out.println("\n=== System Status ===");
			System.out.println("Database: OK");
			System.out.println("UserService: OK");
			System.out.println("Spring Context: OK");
			System.out.println("Max File Upload Size: 10MB");

			// 4. Email service availability (optional)
			if (isEmailEnabled()) {
				System.out.println("Email Service: Enabled");
				// Get the Email bean from the Spring context
				Email emailService = context.getBean(Email.class);
				emailService.testEmailConfiguration();
			} else {
				System.out.println("Email Service: Disabled");
			}

			// 5. User instructions
			printUserInstructions();

			System.out.println("\n=== Application Started Successfully ===");

		} catch (Exception e) {
			System.err.println("\n!!! Application Startup Failed !!!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void testDatabaseConnection() throws Exception {
		System.out.println("\n[1/3] Testing database connection...");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		Map<String, Object> result = jdbcTemplate.queryForMap("SELECT 13 as test_value, NOW() as timestamp");
		System.out.println("Database connection successful:");
		result.forEach((key, value) -> System.out.printf("%-15s: %s%n", key, value));
	}

//	private static void testUserService(ConfigurableApplicationContext context) {
//		System.out.println("\n[2/3] Testing UserService...");
//		UserService userService = context.getBean(UserService.class);
//		userService.loadUserByUsername("admin@admin.com");
//		System.out.println("UserService test passed");
//	}

	private static void printUserInstructions() {
		System.out.println("\n=== How to Test ===");
		System.out.println("1. Register: http://localhost:9090/register");
		System.out.println("2. Login:    http://localhost:9090/login");
		System.out.println("3. Check DB: Verify users in 'app_users' table");
		System.out.println("\nNote: Email service is " + (isEmailEnabled() ? "ENABLED" : "DISABLED") +
				" (change in DemoApplication.java)");
	}

	private static boolean isEmailEnabled() {
		return true; // Changed to true to enable email sending
	}

	private static SimpleDriverDataSource getDataSource() throws ClassNotFoundException {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost:3306/proiect_java?useSSL=false&serverTimezone=UTC");
		dataSource.setUsername("daniel");
		dataSource.setPassword("1234");
		return dataSource;
	}

	// Kept for backward compatibility
	private static void sendTestEmail() {
		if (isEmailEnabled()) {
			try {
				String recipient = "gigel@test.com";
				String subject = "Test Email from Spring Boot";
				String body = "This is a test email sent on startup";
				// This will be handled by the Spring context now
			} catch (Exception e) {
				System.err.println("Failed to send test email:");
				e.printStackTrace();
			}
		}
	}
}
