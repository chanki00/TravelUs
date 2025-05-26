package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditingSession {
    private int id;
    private int planId;
    private int userId;
    private String sessionId;
    private LocalDateTime startedAt;
    private LocalDateTime lastActivity;
    private boolean isActive;
    private String userName;
}
