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
    
    
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteUser(@RequestBody InviteRequest dto) {
    	System.out.println("채팅: " + dto.getChatroomId());
    	chatService.inviteUserToChatroom(dto.getChatroomId(), dto.getInviterId(), dto.getInviteeId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invite/{userId}")
    public ResponseEntity<List<ChatInviteResponse>> getPending(@PathVariable int userId) {
    	System.out.println("유저아이디: " + userId);
        ResponseEntity<List<ChatInviteResponse>> ok = ResponseEntity.ok(chatService.getPendingInvites(userId));
        System.out.println(ok);
        return ok;
    }

    @PatchMapping("/invite/{inviteId}")
    public ResponseEntity<Void> respond(@PathVariable int inviteId, @RequestParam String response) {
    	chatService.respondToInvite(inviteId, response);
        return ResponseEntity.ok().build();
    }
    
 // ChatRestController.java
    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<Integer>> getChatroomIds(@PathVariable int userId) {
        List<Integer> roomIds = chatService.getChatroomIdsByUserId(userId);
        return ResponseEntity.ok(roomIds);
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
    
    @PostMapping("/request")
    public ResponseEntity<Void> requestJoin(@RequestBody InviteRequest dto) {
        chatService.requestToJoinChatroom(dto.getChatroomId(), dto.getInviterId());
        return ResponseEntity.ok().build();
    }
    
 // ChatRestController.java
    @GetMapping("/request/{userId}")
    public ResponseEntity<List<ChatInviteResponse>> getJoinRequests(@PathVariable int userId) {
        return ResponseEntity.ok(chatService.getJoinRequests(userId));
    }


}
