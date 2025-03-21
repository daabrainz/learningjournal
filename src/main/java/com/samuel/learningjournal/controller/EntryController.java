package com.samuel.learningjournal.controller;

import java.util.HashSet;
import java.util.List;
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

    @GetMapping("/home")
    public String index() {
        return "home";
    }
    
    // Alle vorhandenen Einträge anzeigen
    @GetMapping("/entries")
    public String listEntries(Model model) {
        model.addAttribute("entries", entryRepository.findAll()); 
        //TODO 21.03.25: Entries Tags sortieren -> Methode schreiben
        return "list";
    }

    // Neuen Eintrag erstellen
    @GetMapping("/entries/new")
    public String showNewEntryForm(Model model) {
        model.addAttribute("entry", new Entry());
        model.addAttribute("tags", tagRepository.findAllByOrderByNameAsc());
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

    
    @GetMapping("/entries/{id}/delete")
    public String deleteEntry(@PathVariable Long id) {
        entryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Eintrag mit ID: " + id + " nicht gefunden."));
        entryRepository.deleteById(id);
        return "redirect:/entries";
    }
    
    @GetMapping("/entries/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Entry entry = entryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Ungültige Eintrags-ID: " + id));
        model.addAttribute("entry", entry);

        // Alle verfügbaren Tags an das Template übergeben
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);

        return "entry-edit";
    }

    @GetMapping("/entries/{entryId}/tags/{tagId}/add")
    public String addTagToEntry(@PathVariable Long entryId, @PathVariable Long tagId) {
        
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Eintrag mit der ID: " + entryId));

        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Tag mit der ID: " + tagId));

        entry.getTags().add(tag);
        entryRepository.save(entry);

        return "redirect:/entries/" + entryId + "/edit";
 
    }

    @GetMapping("/entries/{entryId}/tags/{tagId}/remove")
    public String removeTagFromEntry(@PathVariable Long entryId, @PathVariable Long tagId) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Eintrag mit der ID: " + entryId));
        
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Tag mit der ID: " + tagId));

        entry.getTags().remove(tag);
        entryRepository.save(entry);

        return "redirect:/entries" + entryId + "/edit";
    }



    @PostMapping("/entries/{id}/edit")
    public String updateEntry(
        @PathVariable Long id, 
        @ModelAttribute Entry entry, 
        @RequestParam(required = false) Set<Long> tagIds) {
       
        Entry existingEntry = entryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Eintrags-ID: " + id));

        existingEntry.setTitle(entry.getTitle());
        existingEntry.setContent(entry.getContent());

        // Tags aktualisieren
        if (tagIds != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            existingEntry.setTags(tags);
        } else {
            existingEntry.setTags(new HashSet<>());
        }

        entryRepository.save(existingEntry);
        return "redirect:/entries";
    }
}
