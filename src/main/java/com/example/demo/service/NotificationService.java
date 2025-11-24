package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification getNotification(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByUser(User user) {
        return notificationRepository.findByUserOrderByScheduledTimeDesc(user);
    }

    public List<Notification> getUnsentNotificationsByUser(User user) {
        return notificationRepository.findByUserAndSent(user, false);
    }

    public List<Notification> getPendingNotifications() {
        return notificationRepository.findByScheduledTimeBeforeAndSent(LocalDateTime.now(), false);
    }

    public Notification markAsSent(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setSent(true);
            return notificationRepository.save(notification);
        }
        return null;
    }

    public void scheduleReminder(User user, String message, LocalDateTime scheduledTime, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setScheduledTime(scheduledTime);
        notification.setSent(false);
        notificationRepository.save(notification);
    }

    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}