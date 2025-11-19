package com.example.demo.controller;

import com.example.demo.model.Alert;
import com.example.demo.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public Alert create(@RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }

    @GetMapping
    public List<Alert> all() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    public Alert get(@PathVariable Long id) {
        return alertService.getAlertById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        alertService.deleteAlert(id);
    }
}
