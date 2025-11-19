package com.example.demo.service;

import com.example.demo.model.CheckIn;
import com.example.demo.model.User;
import com.example.demo.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final Random random = new Random();

    // Predefined reflection questions
    private final List<String> questions = Arrays.asList(
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
    );

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

    public String[] generateRandomQuestions() {
        String[] selected = new String[3];
        List<String> availableQuestions = List.copyOf(questions);

        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(availableQuestions.size());
            selected[i] = availableQuestions.get(index);
        }
        return selected;
    }

    public void deleteCheckIn(Long id) {
        checkInRepository.deleteById(id);
    }
}