package com.example.demo.repository;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndSent(User user, boolean sent);
    List<Notification> findByScheduledTimeBeforeAndSent(LocalDateTime time, boolean sent);
    List<Notification> findByUserOrderByScheduledTimeDesc(User user);
}