package com.example.demo.dto;

public class crisisAlertDTO {

    private Long id;
    private String message;
    private String createdAt;
    private String username;
    private String entryText;
    private boolean crisisConfirmed;

    public crisisAlertDTO(Long id, String message, String createdAt, String username, String entryText, boolean crisisConfirmed) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.username = username;
        this.entryText = entryText;
        this.crisisConfirmed = crisisConfirmed;

    }

    // Getters and setters (or use Lombok if you want)
    public Long getId() { return id; }
    public String getMessage() { return message; }
    public String getCreatedAt() { return createdAt; }
    public String getUsername() { return username; }
    public String getEntryText() { return entryText; }

    public void setId(Long id) { this.id = id; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUsername(String username) { this.username = username; }
    public void setEntryText(String entryText) { this.entryText = entryText; }
    public boolean isCrisisConfirmed() {
        return crisisConfirmed;
    }

    public void setCrisisConfirmed(boolean crisisConfirmed) {
        this.crisisConfirmed = crisisConfirmed;
    }
}