package com.samuel.learningjournal.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.samuel.learningjournal.entity.User;
import com.samuel.learningjournal.repository.UserRepository;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/users";
    };
}
