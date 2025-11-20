package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
            PasswordEncoder encoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user
    public String register(User user, String adminCode) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        // Encode password
        user.setPassword(encoder.encode(user.getPassword()));

        // Assign roles
        Set<String> roles = new HashSet<>();
        roles.add("USER"); // Default role

        // Check for admin code
        if ("admin-secret".equals(adminCode)) {
            roles.add("ADMIN");
        }

        user.setRoles(roles);

        userRepository.save(user);
        return "Registered successfully";
    }

    // Login and return JWT token
    public String login(String username, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = optionalUser.get();
        if (!encoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        // Generate JWT token
        return jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(r -> new org.springframework.security.core.authority.SimpleGrantedAuthority(r)).toList()));
    }
}
