package com.example.demo.controller;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.email.EmailService;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    @Autowired
    private EmailService emailService;

    public NotificationController(NotificationService service) {
        this.service = service;
    }


    @GetMapping("/user/{userId}/unread-count")
    public long unreadCount(@PathVariable Long userId) {
        return service.unreadCount(userId);
    }

    @PostMapping("/mark-read/{id}")
    public void markRead(@PathVariable Long id) {
        service.markRead(id);
    }
}
