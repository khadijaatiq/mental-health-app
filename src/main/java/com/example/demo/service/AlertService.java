package com.example.demo.service;

import com.example.demo.model.Alert;
import com.example.demo.model.User;
import com.example.demo.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert getAlertById(long id) {
        return alertRepository.findById(id).orElse(null);
    }

    public List<Alert> getAlertsByUser(User user) {
        return alertRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Alert> getAlertsBySeverity(String severity) {
        return alertRepository.findBySeverity(severity);
    }

    public List<Alert> getRecentAlerts(LocalDateTime since) {
        return alertRepository.findByCreatedAtAfter(since);
    }

    public void deleteAlert(long id) {
        alertRepository.deleteById(id);
    }
}