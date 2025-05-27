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
import com.mysql.cj.jdbc.Driver;

import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.services",
		"com.example.demo",
		"com.linkDatabase",
		"com.repositories",
		"com.services"
})
@EnableJpaRepositories(basePackages = "com.repositories")
@EnableDiscoveryClient
@EntityScan(basePackages = {
		"com.linkDatabase",
		"com.example.demo"
})

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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SimpleDriverDataSource getConnection(SimpleDriverDataSource dataSource) throws ClassNotFoundException {
    dataSource.setDriverClass(Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost:3306/niciunWeekendAcasa?useSSL=false&allowPublicKeyRetrieval=true");
    dataSource.setUsername("root");
    dataSource.setPassword("1234"); // sau parola corectă
    return dataSource;
	}
}
