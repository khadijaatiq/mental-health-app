package com.example.demo.controller;

import com.example.demo.model.Alert;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.service.AlertService;
import com.example.demo.service.CrisisDetectionService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PostService postService;
    private final AlertService alertService;
    private final CrisisDetectionService crisisDetectionService;
    private  final UserService userService;
    public AdminController(PostService postService, AlertService alertService,
                           CrisisDetectionService crisisDetectionService,
                           UserService userService) {
        this.postService = postService;
        this.alertService = alertService;
        this.crisisDetectionService = crisisDetectionService;
        this.userService = userService;
    }
    @GetMapping("/posts/flagged")
    public ResponseEntity<List<Post>> getFlaggedPosts() {
        List<Post> flaggedPosts = postService.getFlaggedPosts();
        return ResponseEntity.ok(flaggedPosts);
    }
    @PatchMapping("/posts/{id}/flag")
    public ResponseEntity<Post> flagPost(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        Post post = postService.flagPost(id, reason);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/posts/{id}/unflag")
    public ResponseEntity<Post> unflagPost(@PathVariable Long id) {
        Post post = postService.unflagPost(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // Alert Management
    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> getAllAlerts() {
        List<Alert> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/alerts/high-risk")
    public ResponseEntity<List<Alert>> getHighRiskAlerts() {
        List<Alert> alerts = crisisDetectionService.getHighRiskAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/alerts/recent")
    public ResponseEntity<List<Alert>> getRecentAlerts(@RequestParam(defaultValue = "7") int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Alert> alerts = alertService.getRecentAlerts(since);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/alerts/severity/{severity}")
    public ResponseEntity<List<Alert>> getAlertsBySeverity(@PathVariable String severity) {
        List<Alert> alerts = alertService.getAlertsBySeverity(severity);
        return ResponseEntity.ok(alerts);
    }

    // Run crisis detection for a user
    @PostMapping("/crisis-check/{userId}")
    public ResponseEntity<Map<String, String>> runCrisisCheck(@PathVariable Long userId) {
        User user = userService.findById(userId).orElse(null);
        if (user != null) {
            crisisDetectionService.analyzeUserRisk(user);
            return ResponseEntity.ok(Map.of("message", "Crisis check completed for user " + userId));
        }
        return ResponseEntity.notFound().build();
    }

    // Usage statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUsageStats() {
        // You can implement this based on your needs
        return ResponseEntity.ok(Map.of(
                "message", "Usage statistics endpoint",
                "totalUsers", 0, // Implement actual stats
                "totalPosts", postService.getAllPosts().size(),
                "totalAlerts", alertService.getAllAlerts().size()
        ));
    }
}
