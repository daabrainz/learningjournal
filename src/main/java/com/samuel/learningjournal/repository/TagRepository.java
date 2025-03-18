package com.samuel.learningjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuel.learningjournal.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{
}
