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
public class Tripplan {
	private BasicPlanDTO basicplan;
	private int userId;
	private String title;
	private String description;
	private int likes;
	private int shares;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
