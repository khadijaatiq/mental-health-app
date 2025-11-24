package com.example.demo.dto;

import java.time.LocalDateTime;

public class PostDTO {
    private Long id;
    private String content;
    private boolean anonymous;
    private LocalDateTime date;
    private boolean flagged;
    private String flagReason;
    private String username; // Only shown if not anonymous

    public PostDTO() {}

    public PostDTO(Long id, String content, boolean anonymous, LocalDateTime date, boolean flagged, String flagReason, String username) {
        this.id = id;
        this.content = content;
        this.anonymous = anonymous;
        this.date = date;
        this.flagged = flagged;
        this.flagReason = flagReason;
        this.username = username;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public boolean isFlagged() { return flagged; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }
    public String getFlagReason() { return flagReason; }
    public void setFlagReason(String flagReason) { this.flagReason = flagReason; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}