package com.DB_PASSWORD_REDACTED.trip.chat;

import com.DB_PASSWORD_REDACTED.trip.chat.ChatMessage;
import com.DB_PASSWORD_REDACTED.trip.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller("/app")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage handleMessage(@DestinationVariable int roomId, @Payload ChatMessage message) {
        chatService.saveMessage(message); // 💾 DB 저장
        System.out.println("호출됨");
        return message; // 📢 클라이언트 브로드캐스트
    }
}
