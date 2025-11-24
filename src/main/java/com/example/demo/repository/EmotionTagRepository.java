package com.example.demo.repository;

import com.example.demo.model.EmotionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmotionTagRepository extends JpaRepository<EmotionTag, Long> {
    Optional<EmotionTag> findByName(String name);
}
