package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CrisisDetectionService {

    private final MoodService moodService;
    private final StressService stressService;
    private final JournalService journalService;
    private final AlertRepository alertRepository;

    @Value("${app.crisis.keywords}")
    private List<String> keywords;

    @Autowired
    public CrisisDetectionService(MoodService moodService, StressService stressService,
                                  JournalService journalService, AlertRepository alertRepository) {
        this.moodService = moodService;
        this.stressService = stressService;
        this.journalService = journalService;
        this.alertRepository = alertRepository;
    }

    public void analyzeUserRisk(User user) {
        LocalDate now = LocalDate.now();
        LocalDate weekAgo = now.minusWeeks(1);

        // Check mood patterns
        List<Mood> recentMoods = moodService.getMoodsByUserAndDateRange(user, weekAgo.atStartOfDay(),
                now.atTime(23, 59, 59));
        double avgMoodIntensity = recentMoods.stream()
                .mapToInt(Mood::getIntensity)
                .average()
                .orElse(5.0);

        // Check stress levels
        List<Stress> recentStress = stressService.getStressByUserAndDateRange(user, weekAgo, now);
        double avgStress = recentStress.stream()
                .mapToInt(Stress::getStressLevel)
                .average()
                .orElse(5.0);

        // Check journal content for crisis keywords
        List<Journal> recentJournals = journalService.getJournalsByUserAndDateRange(user, weekAgo, now);
        boolean hasCrisisKeywords = recentJournals.stream()
                .anyMatch(j -> containsCrisisKeywords(j.getEntryText()));

        // Generate alerts based on patterns
        if (avgMoodIntensity < 3.0 && avgStress > 7.0) {
            createAlert(user, "Low mood and high stress detected", "HIGH");
        } else if (hasCrisisKeywords) {
            createAlert(user, "Crisis keywords detected in journal", "CRITICAL");
        } else if (avgMoodIntensity < 4.0) {
            createAlert(user, "Consistently low mood detected", "MEDIUM");
        } else if (avgStress > 8.0) {
            createAlert(user, "Very high stress levels detected", "MEDIUM");
        }
    }

    private boolean containsCrisisKeywords(String text) {
        if (text == null || keywords == null)
            return false;
        String lowerText = text.toLowerCase();
        for (String keyword : keywords) {
            if (lowerText.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void createAlert(User user, String reason, String severity) {
        Alert alert = new Alert();
        alert.setUser(user);
        alert.setReason(reason);
        alert.setSeverity(severity);
        alertRepository.save(alert);
    }

    public List<Alert> getHighRiskAlerts() {
        return alertRepository.findBySeverity("CRITICAL");
    }
}