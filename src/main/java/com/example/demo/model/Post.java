package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(nullable = true)
    private boolean anonymous;

    private LocalDateTime date;

    private boolean flagged;
    private String flagReason; // NEW
    private LocalDateTime createdAt; // NEW
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @JsonProperty("userName") // this makes frontend get p.userName
    public String getUserName() {
        // always return actual username for admin
        return user != null ? user.getUsername() : "Unknown";
    }
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.date == null)
            this.date = LocalDateTime.now();
    }
}
