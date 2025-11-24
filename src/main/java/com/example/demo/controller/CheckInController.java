package com.example.demo.controller;

import com.example.demo.model.CheckIn;
import com.example.demo.model.User;
import com.example.demo.service.CheckInService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkins")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping("/questions")
    public ResponseEntity<Map<String, String[]>> getRandomQuestions() {
        String[] questions = checkInService.generateRandomQuestions();
        return ResponseEntity.ok(Map.of("questions", questions));
    }

    @PostMapping
    public ResponseEntity<CheckIn> create(@RequestBody CheckIn checkIn, @AuthenticationPrincipal User user) {
        checkIn.setUser(user);
        CheckIn saved = checkInService.createCheckIn(checkIn);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<CheckIn>> getUserCheckIns(@AuthenticationPrincipal User user) {
        List<CheckIn> checkIns = checkInService.getCheckInsByUser(user);
        return ResponseEntity.ok(checkIns);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CheckIn>> getRecentCheckIns(@AuthenticationPrincipal User user) {
        List<CheckIn> checkins = checkInService.getRecentCheckIns(user);
        return ResponseEntity.ok(checkins);
    }

    @GetMapping("/today/status")
    public ResponseEntity<Map<String, Boolean>> getTodayStatus(@AuthenticationPrincipal User user) {
        LocalDate today = LocalDate.now();
        boolean completed = checkInService.hasCompletedToday(user, today);
        return ResponseEntity.ok(Map.of("completed", completed));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CheckIn> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        CheckIn checkIn = checkInService.getCheckInById(id);
        if (checkIn != null && checkIn.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(checkIn);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        CheckIn checkIn = checkInService.getCheckInById(id);
        if (checkIn != null && checkIn.getUser().getId().equals(user.getId())) {
            checkInService.deleteCheckIn(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}