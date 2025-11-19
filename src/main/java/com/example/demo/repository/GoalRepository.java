package com.example.demo.repository;

import com.example.demo.model.Goal;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(User user);
    List<Goal> findByUserAndStatus(User user, String status);
    List<Goal> findByUserAndTargetDateBefore(User user, LocalDate date);
    List<Goal> findByUserOrderByTargetDateAsc(User user);
}