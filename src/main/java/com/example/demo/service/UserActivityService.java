package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.UserActivity;
import com.example.demo.repository.UserActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<UserActivity> getRecentActivities() {
        return userActivityRepository.findTop50ByOrderByTimestampDesc();
    }
}
