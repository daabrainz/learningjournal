package com.samuel.learningjournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuel.learningjournal.entity.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

}
