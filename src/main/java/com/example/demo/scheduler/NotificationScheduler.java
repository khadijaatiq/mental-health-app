package com.example.demo.scheduler;

import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class NotificationScheduler {
//
//    private final NotificationService notificationService;
//
//    public NotificationScheduler(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    // Run every 5 minutes
//    @Scheduled(cron = "0 */5 * * * *")
//    public void sendPendingNotifications() {
//        List<Notification> pendingNotifications = notificationService.getPendingNotifications();
//
//        for (Notification notification : pendingNotifications) {
//            try {
//                // Simulate sending notification (Email/SMS/Push)
//                sendNotification(notification);
//
//                // Mark as sent
//                notificationService.markAsSent(notification.getId());
//
//                System.out.println("Sent notification: " + notification.getMessage() +
//                        " to user: " + notification.getUser().getUsername());
//            } catch (Exception e) {
//                System.err.println("Failed to send notification " + notification.getId() + ": " + e.getMessage());
//            }
//        }
//    }
//
//    @Autowired
//    private JavaMailSender mailSender; // Requires spring-boot-starter-mail
//
//    private void sendNotification(Notification notification) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(notification.getUser().getEmail());
//        message.setSubject("MindTrack Reminder: " + notification.getType());
//        message.setText(notification.getMessage());
//        mailSender.send(message);
//    }
}