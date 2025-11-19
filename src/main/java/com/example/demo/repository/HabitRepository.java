package com.example.demo.repository;

import com.example.demo.model.Habit;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser(User user);
    List<Habit> findByUserAndDateCompleted(User user, LocalDate date);
    List<Habit> findByUserOrderByCreatedAtDesc(User user);
}