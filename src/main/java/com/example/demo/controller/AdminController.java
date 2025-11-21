package com.example.demo.controller;

import com.example.demo.model.Alert;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.UserActivity;
import com.example.demo.service.AlertService;
import com.example.demo.service.CrisisDetectionService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserActivityService;
import com.example.demo.service.UserService;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final PostService postService;
    private final AlertService alertService;
    private final CrisisDetectionService crisisDetectionService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserActivityService userActivityService;

    public AdminController(PostService postService, AlertService alertService,
            CrisisDetectionService crisisDetectionService,
            UserService userService, UserRepository userRepository,
            UserActivityService userActivityService) {
        this.postService = postService;
        this.alertService = alertService;
        this.crisisDetectionService = crisisDetectionService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userActivityService = userActivityService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long userCount = userRepository.count();
        List<Post> allPosts = postService.getAllPosts();
        long postCount = allPosts.size();
        long flaggedCount = allPosts.stream().filter(Post::isFlagged).count();
        long alertCount = alertService.getAllAlerts().size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userCount);
        stats.put("postCount", postCount);
        stats.put("flaggedCount", flaggedCount);
        stats.put("alertCount", alertCount);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/activity")
    public ResponseEntity<List<UserActivity>> getRecentActivity() {
        return ResponseEntity.ok(userActivityService.getRecentActivities());
    }

    @GetMapping("/posts/flagged")
    public ResponseEntity<List<Post>> getFlaggedPosts() {
        // Using existing service method if available, or stream filter
        // The existing controller used postService.getFlaggedPosts(), let's assume it
        // exists or use stream
        // Checking previous file content, it used postService.getFlaggedPosts().
        // But my PostService view earlier didn't show it explicitly?
        // Wait, I didn't view PostService fully, I viewed PostController.
        // Let's use the stream approach to be safe as I did in my plan, or rely on
        // existing if I'm sure.
        // I'll use the stream approach to be safe and consistent with my previous plan.
        List<Post> flaggedPosts = postService.getAllPosts().stream()
                .filter(Post::isFlagged)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flaggedPosts);
    }

    @PatchMapping("/posts/{id}/flag")
    public ResponseEntity<Post> flagPost(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        // Assuming flagPost exists in service as per previous file
        // If not, I should implement it.
        // To be safe, I'll implement logic here or use service if I knew it existed.
        // The previous file had it, so it likely exists.
        // But I'll use the logic I know works: get, set, update.
        Post post = postService.getPost(id);
        if (post != null) {
            post.setFlagged(true);
            post.setFlagReason(reason);
            return ResponseEntity.ok(postService.updatePost(post));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/posts/{id}/resolve")
    public ResponseEntity<Void> resolveFlag(@PathVariable Long id) {
        Post post = postService.getPost(id);
        if (post != null) {
            post.setFlagged(false);
            post.setFlagReason(null);
            postService.updatePost(post);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // Alert Management (Preserved)
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
}
