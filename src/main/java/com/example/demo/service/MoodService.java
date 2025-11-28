package com.example.demo.service;

import com.example.demo.dto.MoodDTO;
import com.example.demo.model.Mood;
import com.example.demo.model.User;
import com.example.demo.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MoodService {

    private final MoodRepository moodRepository;
    private final UserService userService;

    @Autowired
    public MoodService(MoodRepository moodRepository, UserService userService) {
        this.moodRepository = moodRepository;
        this.userService = userService;
    }

    public Mood createMood(Mood mood) {
        validateMood(mood);
        return moodRepository.save(mood);
    }

    public Mood getMood(Long id) {
        return moodRepository.findById(id).orElse(null);
    }

    public List<Mood> getAllMoods() {
        return moodRepository.findAll();
    }

    // User-specific methods
    public List<Mood> getMoodsByUser(User user) {
        return moodRepository.findByUserOrderByDateDesc(user);
    }

    public List<Mood> getMoodsByUserAndDateRange(User user, LocalDateTime start, LocalDateTime end) {
        return moodRepository.findByUserAndDateBetween(user, start, end);
    }

    public Double getAverageMoodIntensity(User user, LocalDateTime start, LocalDateTime end) {
        return moodRepository.getAverageIntensity(user, start, end);
    }


    public Map<String, Long> getMoodDistribution(User user) {
        List<Object[]> results = moodRepository.countMoodsByUser(user);
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]));
    }

    public Mood updateMood(Mood mood) {
        validateMood(mood);
        return moodRepository.save(mood);
    }

    public void deleteMood(Long id) {
        moodRepository.deleteById(id);
    }

    private void validateMood(Mood mood) {
        if (mood.getIntensity() < 1 || mood.getIntensity() > 10) {
            throw new IllegalArgumentException("Intensity must be between 1 and 10");
        }
    }
}