package com.example.demo.service;

import com.example.demo.model.EmotionTag;
import com.example.demo.repository.EmotionTagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmotionTagService {

    private final EmotionTagRepository emotionTagRepository;

    @Autowired
    public EmotionTagService(EmotionTagRepository emotionTagRepository) {
        this.emotionTagRepository = emotionTagRepository;
    }

    public List<EmotionTag> getAllTags() {
        return emotionTagRepository.findAll();
    }

    public EmotionTag createTag(EmotionTag tag) {
        Optional<EmotionTag> existing = emotionTagRepository.findByName(tag.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Tag with name " + tag.getName() + " already exists.");
        }
        return emotionTagRepository.save(tag);
    }

    public EmotionTag getTagByName(String name) {
        return emotionTagRepository.findByName(name).orElse(null);
    }

    public void deleteTag(long id) {
        if (emotionTagRepository.existsById(id)) {
            emotionTagRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Tag not found with id " + id);
        }
    }
}
