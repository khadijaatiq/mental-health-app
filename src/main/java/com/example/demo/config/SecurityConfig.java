package com.example.demo.config;

import com.example.demo.security.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
        private final JwtFilter jwtFilter;

        public SecurityConfig(JwtFilter jwtFilter) {
                this.jwtFilter = jwtFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                                // Public pages
                                .requestMatchers(
                                        "/",
                                        "/auth/**",
                                        "/api/auth/**",
                                        "/login",
                                        "/register",
                                        "/error/**",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/uploads/**")
                                .permitAll()

                                // USER dashboard & APIs
                                .requestMatchers(
                                        "/dashboard",
                                        "/api/moods/**",
                                        "/api/journals/**",
                                        "/api/habits/**",
                                        "/api/stress/**",
                                        "/api/posts/**",
                                        "/api/notifications/**",
                                        "/api/goals/**",
                                        "/api/export/**",
                                        "/api/checkins/**",
                                        "/resources/**")
                                .hasRole("USER")

                                // ADMIN pages
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                                // Everything else requires authentication
                                .anyRequest().authenticated())
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
        @Bean
        public HandlerMappingIntrospector handlerMappingIntrospector() {
                return new HandlerMappingIntrospector();
        }
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
