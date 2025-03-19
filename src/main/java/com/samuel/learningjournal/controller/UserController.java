package com.samuel.learningjournal.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.samuel.learningjournal.entity.User;
import com.samuel.learningjournal.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwörter stimmen nicht überein. 😢");
            return "register";
        } 
          
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Benutzername existiert bereits. 🫣");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
