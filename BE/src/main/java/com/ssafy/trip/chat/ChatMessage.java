package com.DB_PASSWORD_REDACTED.trip.chat;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessage {
	private int chatroomId;
	private int chatId;
	private int userId;
	private String content;
	private LocalDateTime createdAt = LocalDateTime.now();
}
