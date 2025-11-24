package com.example.demo.config;

import com.example.demo.model.EmotionTag;
import com.example.demo.model.User;
import com.example.demo.repository.EmotionTagRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder, EmotionTagRepository emotionTagRepository) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setName("System Administrator");
                admin.setUsername("admin");
                admin.setEmail("admin@mindtrack.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                Set<String> roles = new HashSet<>();
                roles.add("ROLE_ADMIN");
                admin.setRoles(roles);

                userRepository.save(admin);
                System.out.println("Admin user created: username=admin, password=admin123");
            }
            insert(emotionTagRepository, "GRATEFUL",   "#34d399", EmotionTag.Category.POSITIVE);
            insert(emotionTagRepository, "REFLECTIVE", "#60a5fa", EmotionTag.Category.NEUTRAL);
            insert(emotionTagRepository, "VENTING",    "#f87171", EmotionTag.Category.NEGATIVE);
            insert(emotionTagRepository, "PROUD",      "#fbbf24", EmotionTag.Category.POSITIVE);
            insert(emotionTagRepository, "CONFUSED",   "#a78bfa", EmotionTag.Category.NEUTRAL);
            insert(emotionTagRepository, "ANXIOUS",    "#f43f5e", EmotionTag.Category.NEGATIVE);
            insert(emotionTagRepository, "CALM",       "#3b82f6", EmotionTag.Category.POSITIVE);
        };
    }

    private void insert(EmotionTagRepository repo, String name, String color, EmotionTag.Category category) {
        repo.findByName(name).ifPresentOrElse(
                e -> {}, // already exists
                () -> {
                    EmotionTag tag = new EmotionTag(name, color, category);
                    repo.save(tag);
                    System.out.println("Inserted EmotionTag: " + name);
                }
        );
    }
}
