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
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("entry")
public class EntryController {

    private final EntryRepository entryRepository;
    private final TagRepository tagRepository;

    public EntryController(EntryRepository entryRepository, TagRepository tagRepository) {
        this.entryRepository = entryRepository;
        this.tagRepository = tagRepository;
    }

    @ModelAttribute("entry")
    public Entry entry() {
        return new Entry();
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
    public String showNewEntryForm(Model model, @RequestParam(required = false) Long addTag, @RequestParam(required = false) Long removeTag, @ModelAttribute("entry") Entry entry, SessionStatus status) {


        // Initialisiere die Tag-Liste, falls sie noch nicht existiert
        if (entry.getTags() == null) {
            entry.setTags(new HashSet<>());
        }

        // Tag hinzufügen
        if (addTag != null) {
            Tag tag = tagRepository.findById(addTag)
                .orElseThrow(() -> new IllegalArgumentException("Ungültiger Tag mit der ID: " + addTag));
            if (!entry.getTags().contains(tag)) {
                entry.getTags().add(tag);
            }
        }

        // Tag entfernen
        if (removeTag != null) {
            Tag tag = tagRepository.findById(removeTag)
                .orElseThrow(() -> new IllegalArgumentException("Ungültiger Tag mit der ID: " + removeTag));
            entry.getTags().remove(tag);
        }

        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);
        return "form";
    }

    // Neuen Eintrag zur Datenbank hinzufügen
    @PostMapping("/entries")
    public String addEntry(@ModelAttribute Entry entry, SessionStatus status) {
        
        entryRepository.save(entry);
        status.setComplete();

        return "redirect:/entries";
    }

    // Eintrag löschen
    @GetMapping("/entries/{id}/delete")
    public String deleteEntry(@PathVariable Long id) {
        entryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Eintrag mit ID: " + id + " nicht gefunden."));
        entryRepository.deleteById(id);
        return "redirect:/entries";
    }
    
    // Eintrag bearbeiten
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

    // Tags zu Eintrag hinzufügen
    @GetMapping("/entries/{entryId}/tags/{tagId}/add")
    public String addTagToEntry(@PathVariable Long entryId, @PathVariable Long tagId) {
        
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Eintrag mit der ID: " + entryId));

        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Tag mit der ID: " + tagId));

        entry.getTags().add(tag);
        entryRepository.save(entry);

        return "redirect:/entries/" + entryId + "/edit";
 
    }

    // Tag von Eintrag entfernen
    @GetMapping("/entries/{entryId}/tags/{tagId}/remove")
    public String removeTagFromEntry(@PathVariable Long entryId, @PathVariable Long tagId) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Eintrag mit der ID: " + entryId));
        
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Ungültiger Tag mit der ID: " + tagId));

        entry.getTags().remove(tag);
        entryRepository.save(entry);

        return "redirect:/entries/" + entryId + "/edit";
    }

    // Eintrag aktualisieren
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
            existingEntry.setTags(existingEntry.getTags());
        }

        entryRepository.save(existingEntry);
        return "redirect:/entries";
    }
}
