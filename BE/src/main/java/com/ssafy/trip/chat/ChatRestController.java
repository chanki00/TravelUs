// ChatRestController.java
package com.DB_PASSWORD_REDACTED.trip.chat;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;
import com.DB_PASSWORD_REDACTED.trip.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;
    private final PlanService planService;

    @GetMapping("/{chatroomId}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable int chatroomId) {
        List<ChatMessage> messages = chatService.getMessagesByRoomId(chatroomId);
        return ResponseEntity.ok(messages);
    }
    
    @PostMapping
    public ResponseEntity<Integer> createChatRoom() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        long userId = userDetails.getUser().getId();
    	
        int newRoomId = chatService.createChatRoom((int) userId);
        return ResponseEntity.ok(newRoomId);
    }
    
    @GetMapping("/chatroom/{userId}")
    public ResponseEntity<List<Tripplan>> getChatroomIdByuserId(@PathVariable int userId){
    	List<Integer> myChatroomId = chatService.getChatroomIdByuserId(userId);
    	List<Tripplan> myChatroom = new ArrayList<>();
    	for (Integer chatroomId : myChatroomId) {
			myChatroom.add(planService.getTripplanByChatroomId(chatroomId));
		}
    	return ResponseEntity.ok(myChatroom);
    }
}
