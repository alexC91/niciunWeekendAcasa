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
@EnableDiscoveryClient
@EntityScan(basePackages = {
		"com.linkDatabase",
		"com.repositories",
		"com.example.demo"
})

public class DemoApplication {

	public static void main(String[] args) {
		// Set server port to 9090
		System.setProperty("server.port", "9090");

		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		try {
			System.out.println("\n=== Application Startup Checks ===");


		} catch (Exception e) {
			System.err.println("\n!!! Application Startup Failed !!!");
			e.printStackTrace();
			System.exit(1);
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

	private static void printUserInstructions() {
		System.out.println("\n=== How to Test ===");
		System.out.println("1. Register: http://localhost:9090/register");
		System.out.println("2. Login:    http://localhost:9090/login");
		System.out.println("3. Manual activation: http://localhost:9090/manual-activate?email=user@example.com");
		System.out.println("4. Check DB: Verify users in 'app_users' table");
	}
}
