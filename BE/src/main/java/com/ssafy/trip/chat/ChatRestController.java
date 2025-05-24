// ChatRestController.java
package com.DB_PASSWORD_REDACTED.trip.chat;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/{chatroomId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable int chatroomId) {
        List<ChatMessage> messages = chatService.getMessagesByRoomId(chatroomId);
        return ResponseEntity.ok(messages);
    }
}
