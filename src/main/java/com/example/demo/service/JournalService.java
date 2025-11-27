package com.example.demo.service;

import com.example.demo.model.Journal;
import com.example.demo.model.User;
import com.example.demo.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JournalService {

    private final JournalRepository journalRepository;

    @Autowired
    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Autowired
    private CrisisAlertService crisisAlertService;

    @Value("${app.crisis.keywords}")
    private List<String> keywords;

    public Journal createJournal(Journal journal) {
        Journal saved = journalRepository.save(journal);

        if (containsKeywords(saved.getEntryText())) {
            crisisAlertService.createAlert(saved.getUser(), saved);
        }

        return saved;
    }

    private boolean containsKeywords(String text) {
        if (text == null) return false;

        String lower = text.toLowerCase();
        return keywords.stream().anyMatch(lower::contains);
    }


    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Journal getJournalById(long id) {
        return journalRepository.findById(id).orElse(null);
    }

    // User-specific methods
    public List<Journal> getJournalsByUser(User user) {
        return journalRepository.findByUserOrderByDateDesc(user);
    }

    public List<Journal> getJournalsByUserAndDate(User user, LocalDate date) {
        List<Journal> journals = journalRepository.findByUserIdAndDate(user.getId(), date);
        if (journals.isEmpty()) {
            System.out.println("No journal found for user " + user.getUsername() + " and date " + date);
        } else {
            System.out.println("Found " + journals.size() + " journal(s) for user " + user.getUsername() + " and date " + date);
        }
        return journals;
    }


    public List<Journal> getJournalsByUserAndDateRange(User user, LocalDate start, LocalDate end) {
        return journalRepository.findByUserAndDateBetween(user, start, end);
    }

    public List<Journal> getJournalsByUserAndEmotion(User user, String emotionTag) {
        return journalRepository.findByUserAndEmotionTags_Name(user, emotionTag);
    }

    public Map<String, Long> getEmotionDistribution(User user) {
        List<Object[]> results = journalRepository.countEmotionsByUser(user);
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]));
    }

    public Journal updateJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public void deleteJournal(long id) {
        journalRepository.deleteById(id);
    }
}