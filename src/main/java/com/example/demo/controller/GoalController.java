package com.example.demo.controller;

import com.example.demo.dto.GoalDTO;
import com.example.demo.model.Goal;
import com.example.demo.model.User;
import com.example.demo.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<Goal> create(@RequestBody GoalDTO goalDTO, @AuthenticationPrincipal User user) {
        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(goalDTO.getTitle());
        goal.setDescription(goalDTO.getDescription());
        goal.setTargetDate(goalDTO.getTargetDate());
        goal.setStatus(goalDTO.getStatus() != null ? goalDTO.getStatus() : "pending");

        Goal saved = goalService.createGoal(goal);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getUserGoals(@AuthenticationPrincipal User user) {
        List<Goal> goals = goalService.getGoalsByUser(user);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Goal>> getGoalsByStatus(
            @PathVariable String status,
            @AuthenticationPrincipal User user) {
        List<Goal> goals = goalService.getGoalsByUserAndStatus(user, status);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Goal>> getOverdueGoals(@AuthenticationPrincipal User user) {
        List<Goal> goals = goalService.getOverdueGoals(user);
        return ResponseEntity.ok(goals);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Goal> markAsCompleted(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Goal goal = goalService.getGoalById(id);
        if (goal != null && goal.getUser().getId().equals(user.getId())) {
            Goal completed = goalService.markGoalAsCompleted(id);
            return ResponseEntity.ok(completed);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Goal goal = goalService.getGoalById(id);
        if (goal != null && goal.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(goal);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goal> update(@PathVariable Long id, @RequestBody GoalDTO goalDTO, @AuthenticationPrincipal User user) {
        Goal goal = goalService.getGoalById(id);
        if (goal != null && goal.getUser().getId().equals(user.getId())) {
            goal.setTitle(goalDTO.getTitle());
            goal.setDescription(goalDTO.getDescription());
            goal.setTargetDate(goalDTO.getTargetDate());
            goal.setStatus(goalDTO.getStatus());
            Goal updated = goalService.updateGoal(goal);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Goal goal = goalService.getGoalById(id);
        if (goal != null && goal.getUser().getId().equals(user.getId())) {
            goalService.deleteGoal(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}