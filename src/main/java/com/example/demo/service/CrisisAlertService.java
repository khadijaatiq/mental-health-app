package com.example.demo.service;

import com.example.demo.model.CrisisAlert;
import com.example.demo.model.Journal;
import com.example.demo.model.User;
import com.example.demo.repository.CrisisAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrisisAlertService {

    private final CrisisAlertRepository crisisAlertRepository;

    public CrisisAlertService(CrisisAlertRepository crisisAlertRepository) {
        this.crisisAlertRepository = crisisAlertRepository;
    }

    public CrisisAlert createAlert(User user, Journal journal) {
        CrisisAlert alert = new CrisisAlert();
        alert.setUser(user);
        alert.setJournal(journal);
        alert.setMessage("Potential crisis detected from journal entry.");
        alert.setSeverity("WARNING");
        alert.setTimestamp(LocalDateTime.now());

        return crisisAlertRepository.save(alert);
    }

    public List<CrisisAlert> getAlertsForUser(User user) {
        return crisisAlertRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<CrisisAlert> getActiveAlerts() {
        return crisisAlertRepository.findByResolvedFalse();
    }


    public List<CrisisAlert> getUserAlerts(User user) {
        return crisisAlertRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public CrisisAlert confirmCrisis(Long id, String adminMessage) {
        CrisisAlert alert = crisisAlertRepository.findById(id).orElseThrow();

        alert.setReviewed(true);
        alert.setCrisisConfirmed(true);
        alert.setSeverity("CRISIS");
        alert.setAdminMessage(adminMessage);

        return crisisAlertRepository.save(alert);
    }

    public CrisisAlert resolveAlert(Long id) {
        CrisisAlert alert = crisisAlertRepository.findById(id).orElseThrow();
        alert.setResolved(true);
        return crisisAlertRepository.save(alert);
    }
    public void markResolved(Long id) {
        CrisisAlert alert = crisisAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));

        alert.setResolved(true);  // Assuming you have this field in entity
        crisisAlertRepository.save(alert);
    }

    public List<CrisisAlert> getUnresolvedAlerts() {
        return crisisAlertRepository.findByResolvedFalse();
    }
}
