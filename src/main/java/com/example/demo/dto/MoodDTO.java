package com.example.demo.dto;

import java.time.LocalDate;

public class MoodDTO {
    private Long id;
    private String moodLevel;
    private int intensity;
    private LocalDate date;

    // Constructors, Getters, Setters
    public MoodDTO() {}

    public MoodDTO(Long id, String moodLevel, int intensity, LocalDate date) {
        this.id = id;
        this.moodLevel = moodLevel;
        this.intensity = intensity;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMoodLevel() { return moodLevel; }
    public void setMoodLevel(String moodLevel) { this.moodLevel = moodLevel; }
    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}