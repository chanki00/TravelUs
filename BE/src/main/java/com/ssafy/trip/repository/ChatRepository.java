package com.DB_PASSWORD_REDACTED.trip.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.DB_PASSWORD_REDACTED.trip.chat.ChatMessage;

@Mapper
public interface ChatRepository {
	int getNextChatId(@Param("chatroomId") int chatroomId);
	void insertChatMessage(ChatMessage message);
	
}
