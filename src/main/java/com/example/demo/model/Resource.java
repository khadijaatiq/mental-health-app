package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "resource")
public class Resource {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ResourceCategory category;
    private String link;

    private String fileUrl;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    public Resource() {}

    public Resource(String title, String description, ResourceCategory category, String link, String fileUrl) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.link = link;
        this.fileUrl = fileUrl;
    }
}
