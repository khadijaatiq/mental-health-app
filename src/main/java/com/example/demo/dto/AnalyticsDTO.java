package com.example.demo.dto;

import java.util.Map;

public class AnalyticsDTO {
    private Double averageMoodIntensity;
    private Double averageStressLevel;
    private Map<String, Long> moodCounts;
    private Map<String, Long> emotionCounts;
    private int totalJournals;
    private int totalMoods;
    private int completedGoals;
    private int pendingGoals;

    public AnalyticsDTO() {}

    // Getters and Setters
    public Double getAverageMoodIntensity() { return averageMoodIntensity; }
    public void setAverageMoodIntensity(Double averageMoodIntensity) { this.averageMoodIntensity = averageMoodIntensity; }
    public Double getAverageStressLevel() { return averageStressLevel; }
    public void setAverageStressLevel(Double averageStressLevel) { this.averageStressLevel = averageStressLevel; }
    public Map<String, Long> getMoodCounts() { return moodCounts; }
    public void setMoodCounts(Map<String, Long> moodCounts) { this.moodCounts = moodCounts; }
    public Map<String, Long> getEmotionCounts() { return emotionCounts; }
    public void setEmotionCounts(Map<String, Long> emotionCounts) { this.emotionCounts = emotionCounts; }
    public int getTotalJournals() { return totalJournals; }
    public void setTotalJournals(int totalJournals) { this.totalJournals = totalJournals; }
    public int getTotalMoods() { return totalMoods; }
    public void setTotalMoods(int totalMoods) { this.totalMoods = totalMoods; }
    public int getCompletedGoals() { return completedGoals; }
    public void setCompletedGoals(int completedGoals) { this.completedGoals = completedGoals; }
    public int getPendingGoals() { return pendingGoals; }
    public void setPendingGoals(int pendingGoals) { this.pendingGoals = pendingGoals; }
}