package com.DB_PASSWORD_REDACTED.trip.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotificationResponse {
	private String roomId;          // 채팅방 ID
    private String senderId;        // 보낸 사람 ID
    private String receiverId;      // 받는 사람 ID
    private String type;            // 초대(INVITE) 또는 요청(REQUEST)
    private String tripPlanId;      // 여행 계획 ID
    private String tripPlanTitle;  // 여행 계획 제목

    public boolean isInvitation() {
        return "INVITE".equals(this.type);
    }

    public boolean isRequest() {
        return "REQUEST".equals(this.type);
    }
}
