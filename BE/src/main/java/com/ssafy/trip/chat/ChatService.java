package com.DB_PASSWORD_REDACTED.trip.chat;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	
	private final ChatRepository repo;
	
	public void saveMessage(ChatMessage msg) {
		int nextId = repo.getNextChatId(msg.getChatroomId());
		msg.setChatId(nextId);
		repo.insertChatMessage(msg);
		
	}
	
}
