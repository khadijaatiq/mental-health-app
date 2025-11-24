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

    public Habit getHabitById(long id) {
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
        return habitRepository.findByUserAndLastCompletedDate(user, LocalDate.now());
    }
    public Habit markHabitAsCompleted(long id, LocalDate date) {
        Habit habit = habitRepository.findById(id).orElse(null);
        if (habit == null) return null;

        LocalDate last = habit.getLastCompletedDate();
        LocalDate today = date;

        if (last == null) {
            habit.setStreak(1);
        } else if (last.equals(today)) {
            // already completed today → do nothing
            return habit;
        } else if (last.equals(today.minusDays(1))) {
            habit.setStreak(habit.getStreak() + 1); // consecutive day
        } else {
            habit.setStreak(1); // reset streak
        }

        habit.setLastCompletedDate(today);
        return habitRepository.save(habit);
    }


    public int calculateStreak(User user, String habitName) {
        Habit habit = habitRepository.findByUser(user)
                .stream()
                .filter(h -> h.getHabitName().equals(habitName))
                .findFirst()
                .orElse(null);

        return habit != null ? habit.getStreak() : 0;
    }

    public Habit checkInHabit(Long id) {
        return markHabitAsCompleted(id, LocalDate.now());
    }

    public Habit updateHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public void deleteHabit(long id) {
        habitRepository.deleteById(id);
    }
}