package com.example.demo.controller;

import com.example.demo.dto.HabitDTO;
import com.example.demo.model.Habit;
import com.example.demo.model.User;
import com.example.demo.service.HabitService;
import com.example.demo.service.UserActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;
    private final UserActivityService userActivityService;

    public HabitController(HabitService habitService, UserActivityService userActivityService) {
        this.habitService = habitService;
        this.userActivityService = userActivityService;
    }

    @PostMapping
    public ResponseEntity<Habit> create(@RequestBody HabitDTO habitDTO, @AuthenticationPrincipal User user) {
        Habit habit = new Habit();
        habit.setUser(user);
        habit.setHabitName(habitDTO.getHabitName());
        habit.setFrequency(habitDTO.getFrequency());

        Habit saved = habitService.createHabit(habit);
        userActivityService.logActivity(user, "HABIT_CREATED", "Created habit");
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getUserHabits(@AuthenticationPrincipal User user) {
        List<Habit> habits = habitService.getHabitsByUser(user);
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/today")
    public ResponseEntity<List<Habit>> getTodayCompletedHabits(@AuthenticationPrincipal User user) {
        List<Habit> habits = habitService.getTodayCompletedHabits(user);
        return ResponseEntity.ok(habits);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Habit> markAsCompleted(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal User user) {

        if (date == null) {
            date = LocalDate.now();
        }

        Habit habit = habitService.getHabitById(id);
        if (habit != null && habit.getUser().getId().equals(user.getId())) {
            Habit completed = habitService.markHabitAsCompleted(id, date);
            return ResponseEntity.ok(completed);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/streak")
    public ResponseEntity<Map<String, Integer>> getHabitStreak(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        Habit habit = habitService.getHabitById(id);
        if (habit != null && habit.getUser().getId().equals(user.getId())) {
            int streak = habitService.calculateStreak(user, habit.getHabitName());
            return ResponseEntity.ok(Map.of("streak", streak));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habit> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Habit habit = habitService.getHabitById(id);
        if (habit != null && habit.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(habit);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habit> update(@PathVariable Long id, @RequestBody HabitDTO habitDTO, @AuthenticationPrincipal User user) {
        Habit habit = habitService.getHabitById(id);
        if (habit != null && habit.getUser().getId().equals(user.getId())) {
            habit.setHabitName(habitDTO.getHabitName());
            habit.setFrequency(habitDTO.getFrequency());
            Habit updated = habitService.updateHabit(habit);
            userActivityService.logActivity(user, "HABIT_UPDATED", "Updated habit ID: " + id);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Habit habit = habitService.getHabitById(id);
        if (habit != null && habit.getUser().getId().equals(user.getId())) {
            habitService.deleteHabit(id);
            userActivityService.logActivity(user, "HABIT_DELETED", "Deleted habit ID: " + id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PatchMapping("/{id}/checkin")
    public ResponseEntity<Habit> checkInHabit(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        Habit habit = habitService.getHabitById(id);
        if (habit != null && habit.getUser().getId().equals(user.getId())) {
            Habit completed = habitService.checkInHabit(id);
            userActivityService.logActivity(user, "HABIT_CHECKED_IN", "Checked in habit: " + habit.getHabitName());
            return ResponseEntity.ok(completed);
        }

        return ResponseEntity.notFound().build();
    }

}
