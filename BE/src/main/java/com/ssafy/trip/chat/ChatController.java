// ChatWebSocketController.java
package com.DB_PASSWORD_REDACTED.trip.chat;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage handleMessage(@DestinationVariable int roomId, @Payload ChatMessage message) {
        chatService.saveMessage(message);
        return message;
    }
}
