package com.example.demo.dto;

import com.example.demo.model.Mood;

import java.time.LocalDateTime;

public class MoodDTO {
    private Long id;
    private String moodLevel;
    private int intensity;
    private LocalDateTime date;

    // Constructors, Getters, Setters
    public MoodDTO() {
    }

    public MoodDTO(Long id, String moodLevel, int intensity, LocalDateTime date) {
        this.id = id;
        this.moodLevel = moodLevel;
        this.intensity = intensity;
        this.date = date;
    }
    public static MoodDTO fromEntity(Mood mood) {
        if (mood == null) return null;
        return new MoodDTO(
                mood.getId(),
                mood.getMoodLevel(),
                mood.getIntensity(),
                mood.getDate()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(String moodLevel) {
        this.moodLevel = moodLevel;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}