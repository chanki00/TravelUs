package com.DB_PASSWORD_REDACTED.trip.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCommentDTO {
	private String content;
}
