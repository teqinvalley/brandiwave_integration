package com.teqinvalley.project.user_crud;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

//@EnableSwagger2

@SpringBootApplication
public class UserCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCrudApplication.class, args);
		byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
		String base64Key = Base64.getEncoder().encodeToString(keyBytes);
		System.out.println("Your Base64-encoded secret key:\n" + base64Key);
	}

}
