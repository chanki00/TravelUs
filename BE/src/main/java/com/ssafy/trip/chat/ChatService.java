package com.DB_PASSWORD_REDACTED.trip.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repo;
    private final ChatInviteRepository inviteRpeo;

    public void saveMessage(ChatMessage msg) {
        if (!repo.existsInChatroomUsers(msg.getChatroomId(), msg.getUserId())) {
            System.out.println("없습니다.");
            System.out.println("아이디: " + msg.getUserId());
        	repo.insertChatroomUser(msg.getChatroomId(), msg.getUserId());
        }

        int nextId = repo.getNextChatId(msg.getChatroomId());
        msg.setChatId(nextId);
        repo.insertChatMessage(msg);
    }

    public List<ChatMessage> getMessagesByRoomId(int chatroomId) {
        return repo.getMessagesByRoomId(chatroomId);
    }
    
    @Transactional
    public int createChatRoom(int userId) {
        repo.insertEmptyChatRoom(); // chatroom_id AUTO_INCREMENT
        int newRoomId = repo.getLastInsertedRoomId(); // SELECT LAST_INSERT_ID()
        repo.insertChatroomUser(newRoomId, userId);   // 현재 사용자 참여 등록
        return newRoomId;
    }
    
    
    @Transactional
    public void inviteUserToChatroom(int chatroomId, int inviterId, int inviteeId) {
    	
    	System.out.println("채팅번호: " + chatroomId);
    	
        ChatInvite invite = new ChatInvite();
        invite.setChatroomId(chatroomId);
        invite.setInviterId(inviterId);
        invite.setInviteeId(inviteeId);
        invite.setStatus("PENDING");
        invite.setCreatedAt(LocalDateTime.now());
        inviteRpeo.insertInvite(invite);
    }
    
    public List<ChatInviteResponse> getPendingInvites(int userId) {
        return inviteRpeo.getInvitesByUserId(userId);
    }


    @Transactional
    public void respondToInvite(int inviteId, String response) {
        inviteRpeo.updateInviteStatus(inviteId, response);

        if ("ACCEPTED".equals(response)) {
            ChatInvite invite = inviteRpeo.findById(inviteId);

            // 1. 채팅방 참여
            System.out.println("초대채팅방: " + invite.getChatroomId());
            System.out.println("초대받은유저: " + invite.getInviteeId());
            repo.insertChatroomUser(invite.getChatroomId(), invite.getInviteeId());

            // 🔹 여기까지 하면 해당 유저는 자동으로 여행 계획에도 참여한 것으로 간주됨
            // (chatroom_id를 통해 tripplans와 연결 가능하므로)
        }
    }
    
 // ChatService.java
    public List<Integer> getChatroomIdsByUserId(int userId) {
        return repo.findChatroomIdsByUserId(userId);
    }


}
