package com.example.demo.controller;

import com.example.demo.model.EmotionTag;
import com.example.demo.service.EmotionTagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmotionTag> createTag(@RequestBody EmotionTag tag) {
        try {
            EmotionTag created = emotionTagService.createTag(tag);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        emotionTagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
