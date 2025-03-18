package com.samuel.learningjournal.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.samuel.learningjournal.entity.Entry;
import com.samuel.learningjournal.entity.Tag;
import com.samuel.learningjournal.repository.EntryRepository;
import com.samuel.learningjournal.repository.TagRepository;

import org.springframework.web.bind.annotation.*;

@Controller
public class EntryController {

    private final EntryRepository entryRepository;
    private final TagRepository tagRepository;

    public EntryController(EntryRepository entryRepository, TagRepository tagRepository) {
        this.entryRepository = entryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    // Alle vorhandenen Einträge anzeigen
    @GetMapping("/entries")
    public String listEntries(Model model) {
        model.addAttribute("entries", entryRepository.findAll());
        return "list";
    }

    // Neuen Eintrag erstellen
    @GetMapping("/entries/new")
    public String showNewEntryForm(Model model) {
        model.addAttribute("entry", new Entry());
        model.addAttribute("tags", tagRepository.findAll());
        return "form";
    }

    // Neuen Eintrag zur Datenbank hinzufügen
    @PostMapping("/entries")
    public String addEntry(@RequestParam String title, @RequestParam String content, @RequestParam Set<Long> tagIds) {
        Entry entry = new Entry();
        entry.setTitle(title);
        entry.setContent(content);

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
        entry.setTags(tags);

        entryRepository.save(entry);
        return "redirect:/entries";
    }
    
}
