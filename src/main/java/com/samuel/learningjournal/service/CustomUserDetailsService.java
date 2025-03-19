package com.samuel.learningjournal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.samuel.learningjournal.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User.withUsername(user.getUsername()) // Spring Security UserDetails
                        .password(user.getPassword()) // Passwort bleibt verschlüsselt
                        .roles(user.getRole().replace("ROLE_", "")) // Rolle setzen (ohne "ROLE_" Präfix)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User nicht gefunden"));
    }


}
