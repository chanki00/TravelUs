package com.DB_PASSWORD_REDACTED.trip.chat;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatRepository {

    // 해당 채팅방에서 다음 chat_id 가져오기
    int getNextChatId(@Param("chatroomId") int chatroomId);

    // 채팅 메시지 저장
    void insertChatMessage(ChatMessage message);

    // 해당 유저가 해당 채팅방에 있는지 확인
    boolean existsInChatroomUsers(@Param("chatroomId") int chatroomId, @Param("usersId") int usersId);

    // 채팅방에 유저 등록
    void insertChatroomUser(@Param("chatroomId") int chatroomId, @Param("usersId") int usersId);
    
    List<ChatMessage> getMessagesByRoomId(@Param("chatroomId") int chatroomId);
    
    void insertEmptyChatRoom(); // 채팅방 생성
    int getLastInsertedRoomId(); // 방금 삽입한 채팅방 ID 반환
}
