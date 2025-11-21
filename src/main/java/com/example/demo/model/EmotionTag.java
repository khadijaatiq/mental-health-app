package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "emotion_tags")
public class EmotionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String color; // Hex code, e.g., #FF5733

    @Enumerated(EnumType.STRING)
    private Category category;

    public enum Category {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    public EmotionTag() {
    }

    public EmotionTag(String name, String color, Category category) {
        this.name = name;
        this.color = color;
        this.category = category;
    }
}
