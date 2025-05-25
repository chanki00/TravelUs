package com.DB_PASSWORD_REDACTED.trip.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private int chatroomId;   // 채팅방 ID
    private int chatId;       // 해당 방 내에서의 메시지 순번 (수동 증가)
    private int userId;      // 보낸 사용자 ID
    private String content;   // 메시지 내용
    private LocalDateTime createdAt; // 생성 시간 (옵션)
}
