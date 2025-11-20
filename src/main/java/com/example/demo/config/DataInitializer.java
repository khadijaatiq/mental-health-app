package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setName("System Administrator");
                admin.setUsername("admin");
                admin.setEmail("admin@mindtrack.com");
                admin.setPassword(passwordEncoder.encode("admin123"));

                Set<String> roles = new HashSet<>();
                roles.add("ADMIN");
                roles.add("USER");
                admin.setRoles(roles);

                userRepository.save(admin);
                System.out.println("Admin user created: username=admin, password=admin123");
            }
        };
    }
}
