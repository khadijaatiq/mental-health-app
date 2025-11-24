package com.example.demo.service;

import com.example.demo.dto.AnalyticsDTO;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Map;

@Service
public class AnalyticsService {

    private final MoodService moodService;
    private final JournalService journalService;
    private final StressService stressService;
    private final GoalService goalService;

    @Autowired
    public AnalyticsService(MoodService moodService, JournalService journalService,
            StressService stressService, GoalService goalService) {
        this.moodService = moodService;
        this.journalService = journalService;
        this.stressService = stressService;
        this.goalService = goalService;
    }

    public AnalyticsDTO getUserAnalytics(User user, LocalDate startDate, LocalDate endDate) {
        AnalyticsDTO analytics = new AnalyticsDTO();

        // Mood analytics
        Double avgMoodIntensity = moodService.getAverageMoodIntensity(user, startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59));
        analytics.setAverageMoodIntensity(avgMoodIntensity);

        Map<String, Long> moodCounts = moodService.getMoodDistribution(user);
        analytics.setMoodCounts(moodCounts);

        // Stress analytics
        Double avgStress = stressService.getAverageStressLevel(user, startDate, endDate);
        analytics.setAverageStressLevel(avgStress);

        // Journal analytics
        int totalJournals = journalService.getJournalsByUser(user).size();
        analytics.setTotalJournals(totalJournals);

        Map<String, Long> emotionCounts = journalService.getEmotionDistribution(user);
        analytics.setEmotionCounts(emotionCounts);

        // Goal analytics
        int completedGoals = goalService.getGoalsByUserAndStatus(user, "completed").size();
        int pendingGoals = goalService.getGoalsByUserAndStatus(user, "pending").size();
        analytics.setCompletedGoals(completedGoals);
        analytics.setPendingGoals(pendingGoals);

        int totalMoods = moodService.getMoodsByUser(user).size();
        analytics.setTotalMoods(totalMoods);

        return analytics;
    }

    public Map<String, Object> getWeeklyComparison(User user) {
        LocalDate now = LocalDate.now();
        LocalDate weekAgo = now.minusWeeks(1);
        LocalDate twoWeeksAgo = now.minusWeeks(2);

        Double thisWeekMood = moodService.getAverageMoodIntensity(user, weekAgo.atStartOfDay(), now.atTime(23, 59, 59));
        Double lastWeekMood = moodService.getAverageMoodIntensity(user, twoWeeksAgo.atStartOfDay(),
                weekAgo.atTime(23, 59, 59));

        Double thisWeekStress = stressService.getAverageStressLevel(user, weekAgo, now);
        Double lastWeekStress = stressService.getAverageStressLevel(user, twoWeeksAgo, weekAgo);

        return Map.of(
                "thisWeekMood", thisWeekMood != null ? thisWeekMood : 0.0,
                "lastWeekMood", lastWeekMood != null ? lastWeekMood : 0.0,
                "thisWeekStress", thisWeekStress != null ? thisWeekStress : 0.0,
                "lastWeekStress", lastWeekStress != null ? lastWeekStress : 0.0,
                "moodTrend", calculateTrend(lastWeekMood, thisWeekMood),
                "stressTrend", calculateTrend(lastWeekStress, thisWeekStress));
    }

    private String calculateTrend(Double old, Double current) {
        if (old == null || current == null || old == 0)
            return "neutral";
        double change = ((current - old) / old) * 100;
        return String.format("%.1f%%", change);
    }
}