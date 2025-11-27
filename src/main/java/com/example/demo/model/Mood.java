package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moods")
public class Mood {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String moodLevel;
    private LocalDateTime date;
    private int intensity;
    private LocalDateTime createdAt;
    private int valence;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.date == null)
            this.date = LocalDateTime.now();
    }
}
