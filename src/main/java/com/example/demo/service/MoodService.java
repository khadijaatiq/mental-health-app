package com.example.demo.service;

import com.example.demo.model.Mood;
import com.example.demo.model.User;
import com.example.demo.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MoodService {

    private final MoodRepository moodRepository;

    @Autowired
    public MoodService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    public Mood createMood(Mood mood) {
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

    public List<Mood> getMoodsByUserAndDateRange(User user, LocalDate start, LocalDate end) {
        return moodRepository.findByUserAndDateBetween(user, start, end);
    }

    public Double getAverageMoodIntensity(User user, LocalDate start, LocalDate end) {
        return moodRepository.getAverageIntensity(user, start, end);
    }

    public Map<String, Long> getMoodDistribution(User user) {
        List<Object[]> results = moodRepository.countMoodsByUser(user);
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    public Mood updateMood(Mood mood) {
        return moodRepository.save(mood);
    }

    public void deleteMood(Long id) {
        moodRepository.deleteById(id);
    }
}