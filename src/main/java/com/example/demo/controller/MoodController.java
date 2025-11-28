package com.example.demo.controller;

import com.example.demo.dto.MoodDTO;
import com.example.demo.model.Mood;
import com.example.demo.model.User;
import com.example.demo.service.MoodService;
import com.example.demo.service.UserActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/moods")
public class MoodController {

    private final MoodService moodService;
    private final UserActivityService userActivityService;
    public MoodController(MoodService moodService, UserActivityService userActivityService) {
        this.moodService = moodService;
        this.userActivityService = userActivityService;
    }

    @PostMapping
    public ResponseEntity<Mood> create(@RequestBody MoodDTO moodDTO, @AuthenticationPrincipal User user) {
        Mood mood = new Mood();
        mood.setUser(user);
        mood.setMoodLevel(moodDTO.getMoodLevel());
        mood.setIntensity(moodDTO.getIntensity());
        mood.setDate(moodDTO.getDate());

        Mood saved = moodService.createMood(mood);
        userActivityService.logActivity(user, "MOOD_LOGGED", "Logged mood: " + saved.getMoodLevel());
        return ResponseEntity.ok(saved);
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
    public ResponseEntity<Mood> update(@PathVariable Long id, @RequestBody MoodDTO moodDTO,
                                       @AuthenticationPrincipal User user) {
        Mood mood = moodService.getMood(id);
        if (mood != null && mood.getUser().getId().equals(user.getId())) {
            mood.setMoodLevel(moodDTO.getMoodLevel());
            mood.setIntensity(moodDTO.getIntensity());
            Mood updated = moodService.updateMood(mood);
            userActivityService.logActivity(user, "MOOD_UPDATED", "Updated mood ID: " + id);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MoodDTO>> getRecentMoods(@AuthenticationPrincipal User user) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(7); // last 7 days

        List<Mood> moods = moodService.getMoodsByUserAndDateRange(user, start, end);
        List<MoodDTO> moodDTOs = moods.stream()
                .map(MoodDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(moodDTOs);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Mood mood = moodService.getMood(id);
        if (mood != null && mood.getUser().getId().equals(user.getId())) {
            moodService.deleteMood(id);
            userActivityService.logActivity(user, "MOOD_DELETED", "Deleted mood ID: " + id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
