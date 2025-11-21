package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String message;

    private String type;   // e.g., "REMINDER", "WARNING", "INFO"

    private LocalDateTime scheduledTime;

    private boolean sent;
    private LocalDateTime createdAt; // NEW

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification() {
    }

    public Notification(String message, String type, LocalDateTime scheduledTime, boolean sent) {
        this.message = message;
        this.type = type;
        this.scheduledTime = scheduledTime;
        this.sent = sent;
    }

}
