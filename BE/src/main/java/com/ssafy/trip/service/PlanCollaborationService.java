package com.DB_PASSWORD_REDACTED.trip.service;

import com.DB_PASSWORD_REDACTED.trip.dto.PlanEditingSession;
import com.DB_PASSWORD_REDACTED.trip.dto.ConflictResolution;
import com.DB_PASSWORD_REDACTED.trip.repository.PlanCollaborationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanCollaborationService {
    
    private final PlanCollaborationRepository collaborationRepository;
    
    @Transactional
    public PlanEditingSession startEditingSession(int planId, int userId, String sessionId) {
        // 기존 세션이 있다면 업데이트, 없다면 새로 생성
        PlanEditingSession existingSession = collaborationRepository.findActiveSession(planId, userId);
        
        if (existingSession != null) {
            // 기존 세션 업데이트
            collaborationRepository.updateSessionActivity(planId, userId, sessionId);
            return collaborationRepository.findActiveSession(planId, userId);
        } else {
            // 새 세션 생성
            PlanEditingSession newSession = new PlanEditingSession();
            newSession.setPlanId(planId);
            newSession.setUserId(userId);
            newSession.setSessionId(sessionId);
            newSession.setStartedAt(LocalDateTime.now());
            newSession.setActive(true);
            
            collaborationRepository.createEditingSession(newSession);
            return collaborationRepository.findActiveSession(planId, userId);
        }
    }
    
    @Transactional
    public void endEditingSession(int planId, int userId, String sessionId) {
        collaborationRepository.deactivateSession(planId, userId, sessionId);
    }
    
    public List<PlanEditingSession> getCurrentEditors(int planId) {
        // 5분 이상 비활성 세션은 자동으로 정리
        collaborationRepository.cleanupInactiveSessions(planId, 5);
        return collaborationRepository.findActiveEditors(planId);
    }
    
    public ConflictResolution detectConflict(int planId, int currentVersion, Object newData) {
        // 버전 충돌 감지 로직
        int latestVersion = collaborationRepository.getLatestVersion(planId);
        
        if (currentVersion < latestVersion) {
            // 충돌 발생
            Object currentData = collaborationRepository.getCurrentData(planId);
            
            return new ConflictResolution(
                "VERSION_CONFLICT",
                currentData,
                newData,
                latestVersion,
                currentVersion,
                collaborationRepository.getLastEditor(planId),
                System.currentTimeMillis()
            );
        }
        
        return null; // 충돌 없음
    }
    
    @Transactional
    public void recordChange(int planId, int userId, String changeType, Object oldValue, Object newValue) {
        collaborationRepository.insertChangeHistory(planId, userId, changeType, oldValue, newValue);
    }
}
