package com.example.demo.controller;

import com.example.demo.dto.GoalDTO;
import com.example.demo.model.Goal;
import com.example.demo.model.User;
import com.example.demo.service.GoalService;
import com.example.demo.service.UserActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    private final UserActivityService userActivityService;

    public GoalController(GoalService goalService, UserActivityService userActivityService) {
        this.goalService = goalService;
        this.userActivityService = userActivityService;
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
        userActivityService.logActivity(user, "GOAL_CREATED", "Created goal: " + saved.getTitle());
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
            userActivityService.logActivity(user, "GOAL_COMPLETED", "Completed goal: " + goal.getTitle());
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
            userActivityService.logActivity(user, "GOAL_UPDATED", "Updated goal ID: " + id);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Goal goal = goalService.getGoalById(id);
        if (goal != null && goal.getUser().getId().equals(user.getId())) {
            goalService.deleteGoal(id);
            userActivityService.logActivity(user, "GOAL_DELETED", "Deleted goal ID: " + id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}