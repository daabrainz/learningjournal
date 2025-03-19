package com.samuel.learningjournal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class LearningjournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningjournalApplication.class, args);
		//         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // String rawPassword = "admin123";
        // String encodedPassword = encoder.encode(rawPassword);
        // System.out.println(encodedPassword);
	}

}
