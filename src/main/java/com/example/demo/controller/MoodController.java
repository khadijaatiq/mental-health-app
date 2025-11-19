package com.example.demo.controller;

import com.example.demo.dto.MoodDTO;
import com.example.demo.model.Mood;
import com.example.demo.model.User;
import com.example.demo.service.MoodService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moods")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    @PostMapping
    public ResponseEntity<Mood> create(@RequestBody MoodDTO moodDTO, @AuthenticationPrincipal User user) {
        Mood mood = new Mood();
        mood.setUser(user);
        mood.setMoodLevel(moodDTO.getMoodLevel());
        mood.setIntensity(moodDTO.getIntensity());
        mood.setDate(moodDTO.getDate());

        Mood saved = moodService.createMood(mood);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Mood>> getUserMoods(@AuthenticationPrincipal User user) {
        List<Mood> moods = moodService.getMoodsByUser(user);
        return ResponseEntity.ok(moods);
    }

    @GetMapping("/range")
    public ResponseEntity<List<Mood>> getMoodsByDateRange(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Mood> moods = moodService.getMoodsByUserAndDateRange(user, start, end);
        return ResponseEntity.ok(moods);
    }

    @GetMapping("/average")
    public ResponseEntity<Map<String, Double>> getAverageMood(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Double avg = moodService.getAverageMoodIntensity(user, start, end);
        return ResponseEntity.ok(Map.of("averageIntensity", avg != null ? avg : 0.0));
    }

    @GetMapping("/distribution")
    public ResponseEntity<Map<String, Long>> getMoodDistribution(@AuthenticationPrincipal User user) {
        Map<String, Long> distribution = moodService.getMoodDistribution(user);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mood> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Mood mood = moodService.getMood(id);
        if (mood != null && mood.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(mood);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mood> update(@PathVariable Long id, @RequestBody MoodDTO moodDTO, @AuthenticationPrincipal User user) {
        Mood mood = moodService.getMood(id);
        if (mood != null && mood.getUser().getId().equals(user.getId())) {
            mood.setMoodLevel(moodDTO.getMoodLevel());
            mood.setIntensity(moodDTO.getIntensity());
            Mood updated = moodService.updateMood(mood);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Mood mood = moodService.getMood(id);
        if (mood != null && mood.getUser().getId().equals(user.getId())) {
            moodService.deleteMood(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}