package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Map<String, Object> body,
            @AuthenticationPrincipal User user) {
        String message = (String) body.get("message");
        String type = (String) body.get("type");
        String scheduledTimeStr = (String) body.get("scheduledTime");

        LocalDateTime scheduledTime = LocalDateTime.parse(scheduledTimeStr);

        notificationService.scheduleReminder(user, message, scheduledTime, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(@AuthenticationPrincipal User user) {
        List<Notification> notifications = notificationService.getNotificationsByUser(user);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unsent")
    public ResponseEntity<List<Notification>> getUnsentNotifications(@AuthenticationPrincipal User user) {
        List<Notification> notifications = notificationService.getUnsentNotificationsByUser(user);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Notification notification = notificationService.getNotification(id);
        if (notification != null && notification.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Notification notification = notificationService.getNotification(id);
        if (notification != null && notification.getUser().getId().equals(user.getId())) {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}