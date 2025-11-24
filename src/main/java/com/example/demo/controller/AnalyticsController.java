package com.example.demo.controller;

import com.example.demo.dto.AnalyticsDTO;
import com.example.demo.model.User;
import com.example.demo.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsDTO> getUserAnalytics(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        AnalyticsDTO analytics = analyticsService.getUserAnalytics(user, startDate, endDate);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/weekly-comparison")
    public ResponseEntity<Map<String, Object>> getWeeklyComparison(@AuthenticationPrincipal User user) {
        Map<String, Object> comparison = analyticsService.getWeeklyComparison(user);
        return ResponseEntity.ok(comparison);
    }
}