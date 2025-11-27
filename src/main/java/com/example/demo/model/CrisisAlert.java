package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CrisisAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Journal journal;

    private boolean reviewed = false;
    private boolean crisisConfirmed = false;

    private String adminMessage;
    private String message;  // general alert text
    private String severity; // INFO, WARNING, CRISIS
    private boolean resolved = false;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
