package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserActivityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserActivityService userActivityService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserActivityService userActivityService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.userActivityService = userActivityService;
        this.userRepository = userRepository;
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            User user = new User();
            user.setName(body.get("name"));
            user.setUsername(body.get("username"));
            user.setEmail(body.get("email"));
            user.setPassword(body.get("password"));

            String adminCode = body.get("adminCode");

            String message = authService.register(user, adminCode);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Login endpoint
    @PostMapping("/login")  // FIXED
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            String token = authService.login(username, password);

            User user = userRepository.findByUsernameOrEmail(username, username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");

            userActivityService.logActivity(user, "LOGIN", "User logged in");
            // Set JWT cookie (IMPORTANT)
            Cookie cookie = new Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "admin", isAdmin
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}
