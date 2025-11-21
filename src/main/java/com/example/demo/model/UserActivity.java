package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_activities")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String action; // e.g., "LOGIN", "POST_CREATED"
    private String details; // Optional details
    private LocalDateTime timestamp;

    @PrePersist
    public void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
