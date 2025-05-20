package com.DB_PASSWORD_REDACTED.trip.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ContentSearchRequest {
	private String sido;
	private String gugun;
	private String content;
}
