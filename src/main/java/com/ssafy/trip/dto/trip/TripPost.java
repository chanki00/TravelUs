package com.DB_PASSWORD_REDACTED.trip.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TripPost {
	private int postId;
    private int planId;
    private String title;
    private String author;
    private String content;
    private String createdAt;
    private int views;

}
