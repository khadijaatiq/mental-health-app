package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserActivityService;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body,
            jakarta.servlet.http.HttpServletResponse response) {
        try {
            String username = body.get("username");
            String password = body.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
            }

            String token = authService.login(username, password);

            java.util.Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);
            userOpt.ifPresent(user -> {
                userActivityService.logActivity(user, "LOGIN", "User logged in successfully");
            });

            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("JWT", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Set to true in production
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 day
            response.addCookie(cookie);

            Map<String, Object> responseBody = new java.util.HashMap<>();
            responseBody.put("token", token);
            if (userOpt.isPresent()) {
                responseBody.put("roles", userOpt.get().getRoles());
            }

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
