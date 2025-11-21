package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name="resource")
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
    }
    public Resource() {}

    public Resource(String title, String description, String url, String tag, boolean addedByAdmin) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.tag = tag;
        this.addedByAdmin = addedByAdmin;
    }


}
