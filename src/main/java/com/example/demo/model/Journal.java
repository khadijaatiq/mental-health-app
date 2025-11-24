package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "journals")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"journals", "password", "authorities", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
    private User user;

    @Column(columnDefinition = "TEXT")
    private String entryText;

    @ManyToMany
    @JoinTable(
            name = "journal_emotion_tags",
            joinColumns = @JoinColumn(name = "journal_id"),
            inverseJoinColumns = @JoinColumn(name = "emotion_tag_id")
    )
    @JsonIgnoreProperties({"journals"})
    private Set<EmotionTag> emotionTags = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDate date;

    @PrePersist
    public void onCreate() {
        if (this.date == null)
            this.date = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }
}
