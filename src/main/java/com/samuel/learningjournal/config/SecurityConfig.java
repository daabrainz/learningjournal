package com.samuel.learningjournal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Deaktiviert CSRF-Schutz
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() // Erlaubt Zugriff auf H2-Konsole
                .anyRequest().permitAll()); // Erlaubt alle anderen Anfragen ohne Authentifizierung
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Erlaubt das Einbetten von Frames (für H2-Konsole)
        return http.build();
    }
}
