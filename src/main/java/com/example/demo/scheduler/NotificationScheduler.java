package com.example.demo.scheduler;

import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class NotificationScheduler {

    private final NotificationService notificationService;

    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Run every 5 minutes
    @Scheduled(cron = "0 */5 * * * *")
    public void sendPendingNotifications() {
        List<Notification> pendingNotifications = notificationService.getPendingNotifications();

        for (Notification notification : pendingNotifications) {
            try {
                // Simulate sending notification (Email/SMS/Push)
                sendNotification(notification);

                // Mark as sent
                notificationService.markAsSent(notification.getId());

                System.out.println("Sent notification: " + notification.getMessage() +
                        " to user: " + notification.getUser().getUsername());
            } catch (Exception e) {
                System.err.println("Failed to send notification " + notification.getId() + ": " + e.getMessage());
            }
        }
    }

    private void sendNotification(Notification notification) {
        // TODO: Implement actual notification sending logic
        // For now, just log it
        String type = notification.getType();
        String message = notification.getMessage();
        String username = notification.getUser().getUsername();
        String email = notification.getUser().getEmail();

        System.out.println("=== NOTIFICATION ===");
        System.out.println("Type: " + type);
        System.out.println("To: " + username + " (" + email + ")");
        System.out.println("Message: " + message);
        System.out.println("===================");


    }
}