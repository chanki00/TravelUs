package com.DB_PASSWORD_REDACTED.trip.chat;

import lombok.Data;

@Data
public class InviteRequest {
    private int chatroomId;
    private int inviterId;
    private int inviteeId;
    private String type;  // ✅ 추가
}
