package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    private String tag; // e.g., depression, stress, meditation

    private boolean addedByAdmin;
    private LocalDateTime createdAt; // NEW

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
    public Resource() {}

    public Resource(String title, String description, String url, String tag, boolean addedByAdmin) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.tag = tag;
        this.addedByAdmin = addedByAdmin;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public boolean isAddedByAdmin() { return addedByAdmin; }

    public void setAddedByAdmin(boolean addedByAdmin) { this.addedByAdmin = addedByAdmin; }
}
