package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String habitName;
    private String frequency;
    private LocalDateTime createdAt;
    private LocalDate dateCompleted;
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
