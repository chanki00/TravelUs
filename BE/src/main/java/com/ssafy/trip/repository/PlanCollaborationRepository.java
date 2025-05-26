package com.DB_PASSWORD_REDACTED.trip.repository;

import com.DB_PASSWORD_REDACTED.trip.dto.PlanEditingSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanCollaborationRepository {
    
    // 편집 세션 관리
    void createEditingSession(PlanEditingSession session);
    void updateSessionActivity(@Param("planId") int planId, @Param("userId") int userId, @Param("sessionId") String sessionId);
    void deactivateSession(@Param("planId") int planId, @Param("userId") int userId, @Param("sessionId") String sessionId);
    PlanEditingSession findActiveSession(@Param("planId") int planId, @Param("userId") int userId);
    List<PlanEditingSession> findActiveEditors(@Param("planId") int planId);
    void cleanupInactiveSessions(@Param("planId") int planId, @Param("minutesThreshold") int minutesThreshold);
    
    // 버전 관리
    int getLatestVersion(@Param("planId") int planId);
    Object getCurrentData(@Param("planId") int planId);
    String getLastEditor(@Param("planId") int planId);
    void incrementVersion(@Param("planId") int planId, @Param("userId") int userId);
    
    // 변경 이력
    void insertChangeHistory(@Param("planId") int planId, @Param("userId") int userId, 
                           @Param("changeType") String changeType, @Param("oldValue") Object oldValue, 
                           @Param("newValue") Object newValue);
}
