package com.example.demo.service;

import com.example.demo.model.Goal;
import com.example.demo.model.User;
import com.example.demo.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public Goal createGoal(Goal goal) {
        if (goal.getStatus() == null) {
            goal.setStatus("pending");
        }
        return goalRepository.save(goal);
    }

    public Goal getGoalById(long id) {
        return goalRepository.findById(id).orElse(null);
    }

    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    // User-specific methods
    public List<Goal> getGoalsByUser(User user) {
        return goalRepository.findByUserOrderByTargetDateAsc(user);
    }

    public List<Goal> getGoalsByUserAndStatus(User user, String status) {
        return goalRepository.findByUserAndStatus(user, status);
    }

    public List<Goal> getOverdueGoals(User user) {
        return goalRepository.findByUserAndTargetDateBefore(user, LocalDate.now());
    }

    public Goal markGoalAsCompleted(long id) {
        Goal goal = goalRepository.findById(id).orElse(null);
        if (goal != null) {
            goal.setStatus("completed");
            return goalRepository.save(goal);
        }
        return null;
    }

    public Goal updateGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public void deleteGoal(long id) {
        goalRepository.deleteById(id);
    }
}