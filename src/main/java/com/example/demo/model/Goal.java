package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name="goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDate targetDate;
    private String status; // NEW: pending, completed
    private LocalDateTime createdAt;
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "pending";
    }
    public Goal() {
    }

    public Goal(String title, String description, LocalDate targetDate) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
    }


}
