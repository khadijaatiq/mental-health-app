package com.example.demo.dto;

import java.time.LocalDate;

public class GoalDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate targetDate;
    private String status;

    public GoalDTO() {}

    public GoalDTO(Long id, String title, String description, LocalDate targetDate, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}