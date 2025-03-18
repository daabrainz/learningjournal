package com.samuel.learningjournal.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.samuel.learningjournal.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
