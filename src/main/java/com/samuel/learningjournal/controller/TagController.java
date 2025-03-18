package com.samuel.learningjournal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.samuel.learningjournal.entity.Entry;
import com.samuel.learningjournal.entity.Tag;
import com.samuel.learningjournal.repository.TagRepository;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Alle vorhandenen Tags anzeigen
    @GetMapping
    public String listTags(Model model) {
        model.addAttribute("tags", tagRepository.findAll());
        return "tags";
    }

    // Neuen Tag erstellen
    @GetMapping("/new")
    public String showNewTagForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tag-form";
    }

    // Neuen Tag zur Datenbank hinzufügen
    @PostMapping
    public String addTag(@RequestParam String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
        return "redirect:/tags";
    }

    //Bestimmten Tag löschen
    @GetMapping("/{id}/delete")
    public String deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + id));
        for (Entry entry : tag.getEntries()) {
            entry.getTags().remove(tag);
        }
        tagRepository.deleteById(id);
        return "redirect:/tags";
    }


}
