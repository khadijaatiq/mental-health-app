package com.example.demo.repository;

import com.example.demo.model.Alert;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUser(User user);
    List<Alert> findByUserOrderByCreatedAtDesc(User user);
    List<Alert> findBySeverity(String severity);
    List<Alert> findByCreatedAtAfter(LocalDateTime date);
}