package com.DB_PASSWORD_REDACTED.trip.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
	private int id;
	private int planId;
	private int userId;
	private Date startDate;
	private Date endDate;
	private String title;
	private String content;
	private int currentMembers;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
