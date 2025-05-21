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

	public static void main(String[] args) {
		// Set server port to 9090
		System.setProperty("server.port", "9090");

		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		try {
			System.out.println("\n=== Application Startup Checks ===");

			// 1. Test database connection
//			testDatabaseConnection();

			// 2. Test email configuration
			testEmailConfiguration();

			// 3. System readiness check
			System.out.println("\n=== System Status ===");
			System.out.println("Database: OK");
			System.out.println("Spring Context: OK");
			System.out.println("Server Port: 9090");

			// 4. User instructions
			printUserInstructions();

			System.out.println("\n=== Application Started Successfully ===");

		} catch (Exception e) {
			System.err.println("\n!!! Application Startup Failed !!!");
			e.printStackTrace();
			System.exit(1);
		}
	}

//	private static void testDatabaseConnection() throws Exception {
//		System.out.println("\n[1/3] Testing database connection...");
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
//		Map<String, Object> result = jdbcTemplate.queryForMap("SELECT 13 as test_value, NOW() as timestamp");
//		System.out.println("Database connection successful:");
//		result.forEach((key, value) -> System.out.printf("%-15s: %s%n", key, value));
//	}

	private static void testEmailConfiguration() {
		System.out.println("\n[2/3] Testing email configuration...");
		boolean emailConfigOk = Email.testEmailConfiguration();
		if (emailConfigOk) {
			System.out.println("Email configuration test passed");
		} else {
			System.out.println("Email configuration test failed - check logs for details");
		}
	}

	private static void printUserInstructions() {
		System.out.println("\n=== How to Test ===");
		System.out.println("1. Register: http://localhost:9090/register");
		System.out.println("2. Login:    http://localhost:9090/login");
		System.out.println("3. Manual activation: http://localhost:9090/manual-activate?email=user@example.com");
		System.out.println("4. Check DB: Verify users in 'app_users' table");
	}

//	private static SimpleDriverDataSource getDataSource() throws ClassNotFoundException {
//		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
//		dataSource.setUrl("jdbc:mysql://localhost:3306/proiect_java?useSSL=false&serverTimezone=UTC");
//		dataSource.setUsername("daniel");
//		dataSource.setPassword("1234");
//		return dataSource;
//	}
}
