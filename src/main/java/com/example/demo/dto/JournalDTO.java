package com.example.demo.dto;

import java.time.LocalDate;

public class JournalDTO {
    private Long id;
    private String entryText;
    private String emotionTag;
    private LocalDate date;

    public JournalDTO() {}

    public JournalDTO(Long id, String entryText, String emotionTag, LocalDate date) {
        this.id = id;
        this.entryText = entryText;
        this.emotionTag = emotionTag;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEntryText() { return entryText; }
    public void setEntryText(String entryText) { this.entryText = entryText; }
    public String getEmotionTag() { return emotionTag; }
    public void setEmotionTag(String emotionTag) { this.emotionTag = emotionTag; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}