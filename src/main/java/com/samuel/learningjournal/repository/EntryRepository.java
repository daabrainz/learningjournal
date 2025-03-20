package com.samuel.learningjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samuel.learningjournal.entity.Entry;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    
}
