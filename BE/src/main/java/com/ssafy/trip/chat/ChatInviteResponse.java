package com.DB_PASSWORD_REDACTED.trip.chat;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatInviteResponse {
    private int id;
    private int chatroomId;
    private int inviterId;
    private String name;        // 초대한 사람 이름
    private int inviteeId;
    private String status;      // PENDING, ACCEPTED, REJECTED
    private String type;        // 🔹 초대 유형 (INVITE, REQUEST 등)
    private LocalDateTime createdAt;
}
