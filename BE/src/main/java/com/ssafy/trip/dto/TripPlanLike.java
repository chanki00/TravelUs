package com.DB_PASSWORD_REDACTED.trip.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class TripPlanLike {
    private int id;
    private int planId;
    private int userId;
    private LocalDateTime createdAt;
}
