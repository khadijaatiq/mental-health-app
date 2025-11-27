package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.UserActivity;
import com.example.demo.repository.UserActivityRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    public UserActivityService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    public void logActivity(User user, String action, String details) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setAction(action);
        activity.setDetails(details);
        userActivityRepository.save(activity);
    }


    public Map<String, Object> getActivityTrends(int days) {
        LocalDate today = LocalDate.now();
        LocalDateTime startDate = today.minusDays(days - 1).atStartOfDay();
        LocalDateTime endDate = today.plusDays(1).atStartOfDay();

        List<Object[]> results = userActivityRepository.getActivityCountsByDateAndAction(startDate, endDate);

        // Prepare data structure
        Map<String, Object> trends = new HashMap<>();
        List<String> labels = new ArrayList<>();
        Map<String, List<Integer>> activityCounts = new HashMap<>();

        // Initialize all dates
        for (int i = days - 1; i >= 0; i--) {
            labels.add(today.minusDays(i).toString());
        }

        // Initialize activity types with zeros
        activityCounts.put("logins", new ArrayList<>(Collections.nCopies(days, 0)));
        activityCounts.put("posts", new ArrayList<>(Collections.nCopies(days, 0)));
        activityCounts.put("journals", new ArrayList<>(Collections.nCopies(days, 0)));
        activityCounts.put("moods", new ArrayList<>(Collections.nCopies(days, 0)));
        activityCounts.put("goals", new ArrayList<>(Collections.nCopies(days, 0)));
        activityCounts.put("habits", new ArrayList<>(Collections.nCopies(days, 0)));
        activityCounts.put("stress", new ArrayList<>(Collections.nCopies(days, 0)));

        // Process results from database
        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            String action = (String) row[1];
            Long count = (Long) row[2];

            // Calculate which day index this is
            int dayIndex = days - 1 - (int) java.time.temporal.ChronoUnit.DAYS.between(date, today);

            if (dayIndex >= 0 && dayIndex < days) {
                if (action.equals("LOGIN")) {
                    activityCounts.get("logins").set(dayIndex, count.intValue());
                } else if (action.startsWith("POST_")) {
                    int current = activityCounts.get("posts").get(dayIndex);
                    activityCounts.get("posts").set(dayIndex, current + count.intValue());
                } else if (action.startsWith("JOURNAL_")) {
                    int current = activityCounts.get("journals").get(dayIndex);
                    activityCounts.get("journals").set(dayIndex, current + count.intValue());
                } else if (action.startsWith("MOOD_")) {
                    int current = activityCounts.get("moods").get(dayIndex);
                    activityCounts.get("moods").set(dayIndex, current + count.intValue());
                } else if (action.startsWith("GOAL_")) {
                    int current = activityCounts.get("goals").get(dayIndex);
                    activityCounts.get("goals").set(dayIndex, current + count.intValue());
                } else if (action.startsWith("HABIT_")) {
                    int current = activityCounts.get("habits").get(dayIndex);
                    activityCounts.get("habits").set(dayIndex, current + count.intValue());
                } else if (action.startsWith("STRESS_")) {
                    int current = activityCounts.get("stress").get(dayIndex);
                    activityCounts.get("stress").set(dayIndex, current + count.intValue());
                }
            }
        }

        // Put everything into the response map
        trends.put("labels", labels);
        trends.put("logins", activityCounts.get("logins"));
        trends.put("posts", activityCounts.get("posts"));
        trends.put("journals", activityCounts.get("journals"));
        trends.put("moods", activityCounts.get("moods"));
        trends.put("goals", activityCounts.get("goals"));
        trends.put("habits", activityCounts.get("habits"));
        trends.put("stress", activityCounts.get("stress"));

        return trends;
    }
    public List<UserActivity> getRecentActivities() {
        return userActivityRepository.findTop50ByOrderByTimestampDesc();
    }
}
