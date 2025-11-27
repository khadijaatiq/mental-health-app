package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserActivityService userActivityService;
    private final CheckInService checkInService;
    private final MoodRepository moodRepository;
    private final JournalRepository journalRepository;
    private final GoalRepository goalRepository;
    private final HabitRepository habitRepository;
    private final StressRepository stressRepository;

    public AdminController(PostService postService,
                           UserService userService,
                           UserRepository userRepository,
                           UserActivityService userActivityService,
                           CheckInService checkInService,
                           MoodRepository moodRepository,
                           JournalRepository journalRepository,
                           GoalRepository goalRepository,
                           HabitRepository habitRepository,
                           StressRepository stressRepository) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userActivityService = userActivityService;
        this.checkInService = checkInService;
        this.moodRepository = moodRepository;
        this.journalRepository = journalRepository;
        this.goalRepository = goalRepository;
        this.habitRepository = habitRepository;
        this.stressRepository = stressRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long userCount = userRepository.count();
        List<Post> allPosts = postService.getAllPosts();
        long postCount = allPosts.size();
        long flaggedCount = allPosts.stream().filter(Post::isFlagged).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userCount);
        stats.put("postCount", postCount);
        stats.put("flaggedCount", flaggedCount);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/trends")
    public ResponseEntity<Map<String, Object>> getUsageTrends(
            @RequestParam(defaultValue = "7") int days) {
        Map<String, Object> trends = userActivityService.getActivityTrends(days);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PatchMapping("/users/{id}/block")
    public ResponseEntity<User> blockUser(@PathVariable long id, @RequestBody Map<String, Boolean> body) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            boolean blocked = body.getOrDefault("blocked", true);
            user.setBlocked(blocked);
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            String role = body.get("role");
            if (role != null) {
                user.setRoles(new HashSet<>(List.of("ROLE_USER")));
                return ResponseEntity.ok(userRepository.save(user));
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/activity")
    public ResponseEntity<List<UserActivity>> getRecentActivity() {
        return ResponseEntity.ok(userActivityService.getRecentActivities());
    }

    @GetMapping("/posts/flagged")
    public ResponseEntity<List<Post>> getFlaggedPosts() {
        List<Post> flaggedPosts = postService.getAllPosts().stream()
                .filter(Post::isFlagged)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flaggedPosts);
    }

    @PatchMapping("/posts/{id}/flag")
    public ResponseEntity<Post> flagPost(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        Post post = postService.getPost(id);
        if (post != null) {
            post.setFlagged(true);
            post.setFlagReason(reason);
            return ResponseEntity.ok(postService.updatePost(post));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPostsAdmin() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // Global Settings - Check-in Questions
    @GetMapping("/settings/checkin-questions")
    public ResponseEntity<List<String>> getCheckInQuestions() {
        return ResponseEntity.ok(checkInService.getAllQuestions());
    }

    @PostMapping("/settings/checkin-questions")
    public ResponseEntity<Void> addCheckInQuestion(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question != null && !question.isBlank()) {
            checkInService.addQuestion(question);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/settings/checkin-questions")
    public ResponseEntity<Void> removeCheckInQuestion(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question != null) {
            checkInService.removeQuestion(question);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // System-wide Export
    @GetMapping("/export/system")
    public ResponseEntity<String> exportSystemData() {
        StringBuilder csv = new StringBuilder();
        csv.append("Type,User,Date,Details\n");

        // Moods
        List<Mood> moods = moodRepository.findAll();
        for (Mood m : moods) {
            csv.append("Mood,").append(m.getUser().getUsername()).append(",")
                    .append(m.getDate()).append(",").append("Level: ").append(m.getMoodLevel()).append("\n");
        }

        // Stress
        List<Stress> stressList = stressRepository.findAll();
        for (Stress s : stressList) {
            csv.append("Stress,").append(s.getUser().getUsername()).append(",")
                    .append(s.getDate()).append(",").append("Level: ").append(s.getStressLevel()).append("\n");
        }

        // Journals
        List<Journal> journals = journalRepository.findAll();
        for (Journal j : journals) {
            csv.append("Journal,").append(j.getUser().getUsername()).append(",")
                    .append(j.getDate()).append(",").append("Entry ID: ").append(j.getId()).append("\n");
        }

        return createCsvResponse(csv.toString(), "system-data.csv");
    }

    @GetMapping("/export/moods")
    public ResponseEntity<String> exportMoods() {
        StringBuilder csv = new StringBuilder();
        csv.append("User,Date,Mood,Intensity\n");
        List<Mood> moods = moodRepository.findAll();
        for (Mood m : moods) {
            csv.append(m.getUser().getUsername()).append(",")
                    .append(m.getDate()).append(",")
                    .append(m.getMoodLevel()).append(",")
                    .append(m.getIntensity()).append("\n");
        }
        return createCsvResponse(csv.toString(), "mood-logs.csv");
    }

    @GetMapping("/export/stress")
    public ResponseEntity<String> exportStress() {
        StringBuilder csv = new StringBuilder();
        csv.append("User,Date,Level,Notes\n");
        List<Stress> stressList = stressRepository.findAll();
        for (Stress s : stressList) {
            csv.append(s.getUser().getUsername()).append(",")
                    .append(s.getDate()).append(",")
                    .append(s.getStressLevel()).append(",")
                    .append(escapeCsv(s.getNotes())).append("\n");
        }
        return createCsvResponse(csv.toString(), "stress-logs.csv");
    }

    @GetMapping("/export/users")
    public ResponseEntity<String> exportUsers() {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Username,Email,Role,Blocked\n");
        List<User> users = userRepository.findAll();
        for (User u : users) {
            csv.append(u.getId()).append(",")
                    .append(u.getUsername()).append(",")
                    .append(u.getEmail()).append(",")
                    .append(u.getRole()).append(",")
                    .append(u.isBlocked()).append("\n");
        }
        return createCsvResponse(csv.toString(), "users.csv");
    }

    private ResponseEntity<String> createCsvResponse(String csvData, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(csvData);
    }

    private String escapeCsv(String data) {
        if (data == null)
            return "";
        return "\"" + data.replace("\"", "\"\"") + "\"";
    }

    // Database Backup (Mock)
    @GetMapping("/backup")
    public ResponseEntity<Map<String, String>> triggerBackup() {
        return ResponseEntity
                .ok(Map.of("message", "Database backup triggered successfully. Backup file created at /backups/db-"
                        + System.currentTimeMillis() + ".sql"));
    }

    // CRUD for other entities
    @DeleteMapping("/moods/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Long id) {
        moodRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/journals/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable Long id) {
        journalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/habits/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/board")
    public String boardPage(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "admin/board";
    }

    @GetMapping("/flagged")
    public String flaggedPage() {
        return "admin/flagged";
    }
}