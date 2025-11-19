package com.example.demo.service;

import com.example.demo.model.Habit;
import com.example.demo.model.User;
import com.example.demo.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    @Autowired
    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public Habit createHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public Habit getHabitById(Long id) {
        return habitRepository.findById(id).orElse(null);
    }

    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    // User-specific methods
    public List<Habit> getHabitsByUser(User user) {
        return habitRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Habit> getTodayCompletedHabits(User user) {
        return habitRepository.findByUserAndDateCompleted(user, LocalDate.now());
    }

    public Habit markHabitAsCompleted(Long id, LocalDate date) {
        Habit habit = habitRepository.findById(id).orElse(null);
        if (habit != null) {
            habit.setDateCompleted(date);
            return habitRepository.save(habit);
        }
        return null;
    }

    public int calculateStreak(User user, String habitName) {
        List<Habit> habits = habitRepository.findByUser(user);
        // Simple streak calculation - count consecutive days
        int streak = 0;
        LocalDate checkDate = LocalDate.now();

        for (int i = 0; i < 30; i++) { // Check last 30 days
            final LocalDate currentDate = checkDate;
            boolean completedOnDate = habits.stream()
                    .anyMatch(h -> h.getHabitName().equals(habitName) &&
                            h.getDateCompleted() != null &&
                            h.getDateCompleted().equals(currentDate));

            if (completedOnDate) {
                streak++;
                checkDate = checkDate.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }

    public Habit updateHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public void deleteHabit(Long id) {
        habitRepository.deleteById(id);
    }
}