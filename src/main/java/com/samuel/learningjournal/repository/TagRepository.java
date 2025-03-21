package com.samuel.learningjournal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuel.learningjournal.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
    List<Tag> findAllByOrderByNameAsc();
}
