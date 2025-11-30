package com.example.demo.service;

import com.example.demo.model.CheckIn;
import com.example.demo.model.User;
import com.example.demo.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;

    // Predefined reflection questions
    private final List<String> questions = new ArrayList<>(Arrays.asList(
            "How are you feeling today?",
            "What's one thing you're grateful for?",
            "What challenged you today?",
            "What brought you joy today?",
            "How would you rate your stress level today?",
            "What's one thing you accomplished today?",
            "How well did you take care of yourself today?",
            "What's on your mind right now?",
            "What do you need more of in your life?",
            "What's one thing you're looking forward to?"
    ));

    @Autowired
    public CheckInService(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    public CheckIn createCheckIn(CheckIn checkIn) {
        return checkInRepository.save(checkIn);
    }

    public List<CheckIn> getCheckInsByUser(User user) {
        return checkInRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public CheckIn getCheckInById(Long id) {
        return checkInRepository.findById(id).orElse(null);
    }

    public List<CheckIn> getRecentCheckIns(User user) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        return checkInRepository.findRecentCheckIns(user.getId(), threeDaysAgo);
    }

    public boolean hasCompletedToday(User user, LocalDate date) {
        List<CheckIn> checkIns = checkInRepository.findByUserAndDate(user, date);
        System.out.println("Checking for user: " + user.getUsername() + " on date: " + date);
        System.out.println("Found check-ins: " + checkIns.size());
        return !checkIns.isEmpty();
    }

    public String[] generateRandomQuestions() {
        List<String> shuffled = new java.util.ArrayList<>(questions);
        java.util.Collections.shuffle(shuffled);

        return new String[] {
                shuffled.get(0),
                shuffled.get(1),
                shuffled.get(2)
        };
    }

    public void deleteCheckIn(Long id) {
        checkInRepository.deleteById(id);
    }

    public void addQuestion(String question) {
        questions.add(question);
    }

    public void removeQuestion(String question) {
        questions.remove(question);
    }

    public List<String> getAllQuestions() {
        return questions;
    }
}