package com.DB_PASSWORD_REDACTED.trip.chat;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repo;

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
}
