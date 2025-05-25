package com.DB_PASSWORD_REDACTED.trip.chat;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatInviteResponse {
    private int id;
    private int chatroomId;
    private int inviterId;
    private String name;  // ✅ 추가
    private int inviteeId;
    private String status;
    private LocalDateTime createdAt;
}
