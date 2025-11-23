package com.example.demo.controller;

import com.example.demo.dto.JournalDTO;
import com.example.demo.model.EmotionTag;
import com.example.demo.model.Journal;
import com.example.demo.model.User;
import com.example.demo.service.EmotionTagService;
import com.example.demo.service.JournalService;
import com.example.demo.service.UserActivityService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    private final JournalService journalService;
    private final EmotionTagService emotionTagService;

    private final UserActivityService userActivityService;

    public JournalController(JournalService journalService, EmotionTagService emotionTagService, UserActivityService userActivityService) {
        this.journalService = journalService;
        this.emotionTagService = emotionTagService;
        this.userActivityService = userActivityService;
    }

    @PostMapping
    public ResponseEntity<Journal> create(@RequestBody JournalDTO journalDTO, @AuthenticationPrincipal User user) {
        Journal journal = new Journal();
        journal.setUser(user);
        journal.setEntryText(journalDTO.getEntryText());
        journal.setDate(journalDTO.getDate());

        if (journalDTO.getEmotionTags() != null) {
            Set<EmotionTag> tags = new HashSet<>();
            for (String tagName : journalDTO.getEmotionTags()) {
                EmotionTag tag = emotionTagService.getTagByName(tagName);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            journal.setEmotionTags(tags);
        }

        Journal saved = journalService.createJournal(journal);
        userActivityService.logActivity(user, "JOURNAL_CREATED", "Created journal entry for " + saved.getDate());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getUserJournals(@AuthenticationPrincipal User user) {
        List<Journal> journals = journalService.getJournalsByUser(user);
        return ResponseEntity.ok(journals);
    }

    @GetMapping("/range")
    public ResponseEntity<List<Journal>> getJournalsByDateRange(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Journal> journals = journalService.getJournalsByUserAndDateRange(user, start, end);
        return ResponseEntity.ok(journals);
    }

    @GetMapping("/emotions/{emotion}")
    public ResponseEntity<List<Journal>> getJournalsByEmotion(
            @PathVariable String emotion,
            @AuthenticationPrincipal User user) {
        List<Journal> journals = journalService.getJournalsByUserAndEmotion(user, emotion);
        return ResponseEntity.ok(journals);
    }

    @GetMapping("/date")
    public ResponseEntity<List<Journal>> getJournalByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                    @AuthenticationPrincipal User user) {

        System.out.println("Received getJournalByDate request for date: " + date + ", user: " + user.getUsername());
        List<Journal> journals = journalService.getJournalsByUserAndDate(user, date);
        if (journals.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(journals);
    }

    @GetMapping("/emotion-distribution")
    public ResponseEntity<Map<String, Long>> getEmotionDistribution(@AuthenticationPrincipal User user) {
        Map<String, Long> distribution = journalService.getEmotionDistribution(user);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/{id}")
    @Transactional // Add this annotation
    public ResponseEntity<Journal> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Journal journal = journalService.getJournalById(id);
        if (journal != null && journal.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(journal);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Journal> update(@PathVariable Long id, @RequestBody JournalDTO journalDTO,
            @AuthenticationPrincipal User user) {
        Journal journal = journalService.getJournalById(id);
        if (journal != null && journal.getUser().getId().equals(user.getId())) {
            journal.setEntryText(journalDTO.getEntryText());

            if (journalDTO.getEmotionTags() != null) {
                Set<EmotionTag> tags = new HashSet<>();
                for (String tagName : journalDTO.getEmotionTags()) {
                    EmotionTag tag = emotionTagService.getTagByName(tagName);
                    if (tag != null) {
                        tags.add(tag);
                    }
                }
                journal.setEmotionTags(tags);
            }

            Journal updated = journalService.updateJournal(journal);
            userActivityService.logActivity(user, "JOURNAL_UPDATED", "Updated journal entry ID: " + id);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Journal journal = journalService.getJournalById(id);
        if (journal != null && journal.getUser().getId().equals(user.getId())) {
            journalService.deleteJournal(id);
            userActivityService.logActivity(user, "JOURNAL_DELETED", "Deleted journal entry ID: " + id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
