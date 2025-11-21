package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean anonymous;

    private LocalDateTime date;

    private boolean flagged;
    private String flagReason; // NEW
    private LocalDateTime createdAt; // NEW

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.date == null) this.date = LocalDateTime.now();
    }

}
