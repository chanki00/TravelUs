package com.DB_PASSWORD_REDACTED.trip.dto.comment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class InsertCommentDTO {
	private int planId;
	private int userId;
	private String content;
}
