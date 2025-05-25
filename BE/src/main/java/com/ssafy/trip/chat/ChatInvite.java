package com.DB_PASSWORD_REDACTED.trip.chat;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatInvite {
    private int id; // PK
    private int chatroomId;
    private int inviterId;
    private int inviteeId;
    private String status;     // PENDING, ACCEPTED, REJECTED
    private String type;       // ✅ INVITE, REQUEST
    private LocalDateTime createdAt;
}