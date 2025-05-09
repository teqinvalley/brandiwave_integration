package com.teqinvalley.project.user_crud;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Base64;

//@EnableSwagger2

@SpringBootApplication
@EnableScheduling
public class UserCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCrudApplication.class, args);
		byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
		String base64Key = Base64.getEncoder().encodeToString(keyBytes);
		System.out.println("Your Base64-encoded secret key:\n" + base64Key);
	}
	@Scheduled(fixedRate = 5000) // Runs every 5 seconds
	public void scheduledTask() {
		System.out.println("Scheduled Task executed at: " + System.currentTimeMillis());
	}

	// A scheduled task using a cron expression (daily at 12 PM)
	@Scheduled(cron = "0 0 12 * * ?") // Executes every day at 12 PM
	public void scheduledCronTask() {
		System.out.println("Scheduled Task executed at 12 PM: " + System.currentTimeMillis());
	}

}
