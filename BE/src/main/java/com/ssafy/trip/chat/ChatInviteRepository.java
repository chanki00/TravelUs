package com.DB_PASSWORD_REDACTED.trip.chat;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface ChatInviteRepository {
    void insertInvite(ChatInvite invite);
    List<ChatInviteResponse> getInvitesByUserId(int userId);
    void updateInviteStatus(@Param("id") int id, @Param("status") String status);
    ChatInvite findById(int id);
}
