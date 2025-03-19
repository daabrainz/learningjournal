package com.samuel.learningjournal.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.samuel.learningjournal.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
