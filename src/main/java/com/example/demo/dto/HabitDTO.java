package com.example.demo.dto;

import java.time.LocalDate;

public class HabitDTO {
    private Long id;
    private String habitName;
    private String frequency;
    private LocalDate dateCompleted;
    private int currentStreak;

    public HabitDTO() {}

    public HabitDTO(Long id, String habitName, String frequency, LocalDate dateCompleted, int currentStreak) {
        this.id = id;
        this.habitName = habitName;
        this.frequency = frequency;
        this.dateCompleted = dateCompleted;
        this.currentStreak = currentStreak;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHabitName() { return habitName; }
    public void setHabitName(String habitName) { this.habitName = habitName; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public LocalDate getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(LocalDate dateCompleted) { this.dateCompleted = dateCompleted; }
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
}