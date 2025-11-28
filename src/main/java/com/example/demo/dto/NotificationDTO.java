package com.example.demo.dto;

import java.time.Instant;

public record NotificationDTO(Long id, Long userId, String type, String message, String link, boolean isRead, Instant createdAt) {



}
