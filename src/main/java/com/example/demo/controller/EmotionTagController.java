package com.example.demo.controller;

import com.example.demo.model.EmotionTag;
import com.example.demo.service.EmotionTagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class EmotionTagController {

    private final EmotionTagService emotionTagService;

    public EmotionTagController(EmotionTagService emotionTagService) {
        this.emotionTagService = emotionTagService;
    }

    @GetMapping
    public ResponseEntity<List<EmotionTag>> getAllTags() {
        return ResponseEntity.ok(emotionTagService.getAllTags());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmotionTag> createTag(@RequestBody EmotionTag tag) {
        try {
            EmotionTag created = emotionTagService.createTag(tag);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        try {
            emotionTagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
