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

//			/// Testare email verify
//			NewAccountMail mailController = context.getBean(NewAccountMail.class);
//			// Simulate user registration
//			String testName = "TestUser";
//			String testEmail = "Vmihai739@gmail.com";
//			String registerResult = mailController.registerUser(testName, testEmail);
//			System.out.println("Registration result: " + registerResult);
//			// Retrieve the generated token for that email (for testing purposes)
//			String token = mailController.getTokenForEmail(testEmail);
//			System.out.println("Simulated token: " + token);
//
//			// NU verifica contul aici! Lasa userul să apese pe linkul real din email
//			System.out.println("Accesează linkul din email pentru a finaliza verificarea.");
//			/// Final testare

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
