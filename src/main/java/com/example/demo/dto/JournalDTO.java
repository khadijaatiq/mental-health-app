package com.example.demo.dto;

import lombok.Setter;

import java.time.LocalDate;

@Setter
public class JournalDTO {
    private Long id;
    private String entryText;
    private java.util.List<String> emotionTags;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public java.util.List<String> getEmotionTags() {
        return emotionTags;
    }

    public String getEntryText() {
        return entryText;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}