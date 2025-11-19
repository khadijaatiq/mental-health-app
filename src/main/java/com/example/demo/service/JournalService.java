package com.example.demo.service;

import com.example.demo.model.Journal;
import com.example.demo.model.User;
import com.example.demo.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Journal createJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }

    public Journal getJournalById(Long id) {
        return journalRepository.findById(id).orElse(null);
    }

    // User-specific methods
    public List<Journal> getJournalsByUser(User user) {
        return journalRepository.findByUserOrderByDateDesc(user);
    }

    public Optional<Journal> getJournalByUserAndDate(User user, LocalDate date) {
        return journalRepository.findByUserAndDate(user, date);
    }

    public List<Journal> getJournalsByUserAndDateRange(User user, LocalDate start, LocalDate end) {
        return journalRepository.findByUserAndDateBetween(user, start, end);
    }

    public List<Journal> getJournalsByUserAndEmotion(User user, String emotionTag) {
        return journalRepository.findByUserAndEmotionTag(user, emotionTag);
    }

    public Map<String, Long> getEmotionDistribution(User user) {
        List<Object[]> results = journalRepository.countEmotionsByUser(user);
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    public Journal updateJournal(Journal journal) {
        return journalRepository.save(journal);
    }

    public void deleteJournal(Long id) {
        journalRepository.deleteById(id);
    }
}