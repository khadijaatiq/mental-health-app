package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private User user;

    @Column(columnDefinition = "TEXT")
    private String entryText;

    @ManyToMany
    @JoinTable(name = "journal_emotion_tags", joinColumns = @JoinColumn(name = "journal_id"), inverseJoinColumns = @JoinColumn(name = "emotion_tag_id"))
    private java.util.Set<EmotionTag> emotionTags = new java.util.HashSet<>();
    private LocalDateTime createdAt;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    public java.util.Set<EmotionTag> getEmotionTags() {
        return emotionTags;
    }

    public void setEmotionTags(java.util.Set<EmotionTag> emotionTags) {
        this.emotionTags = emotionTags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @PrePersist
    public void onCreate() {
        if (this.date == null)
            this.date = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }
}
