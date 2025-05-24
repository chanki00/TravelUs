package com.DB_PASSWORD_REDACTED.trip.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repo;

    public void saveMessage(ChatMessage msg) {
        if (!repo.existsInChatroomUsers(msg.getChatroomId(), msg.getUsersId())) {
            repo.insertChatroomUser(msg.getChatroomId(), msg.getUsersId());
        }

        int nextId = repo.getNextChatId(msg.getChatroomId());
        msg.setChatId(nextId);
        repo.insertChatMessage(msg);
    }

    public List<ChatMessage> getMessagesByRoomId(int chatroomId) {
        return repo.getMessagesByRoomId(chatroomId);
    }
}
