package com.DB_PASSWORD_REDACTED.trip.controller;

import com.DB_PASSWORD_REDACTED.trip.dto.PlanChangeEvent;
import com.DB_PASSWORD_REDACTED.trip.dto.PlanEditingSession;
import com.DB_PASSWORD_REDACTED.trip.service.PlanCollaborationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PlanCollaborationController {

    private final PlanCollaborationService collaborationService;
    private final SimpMessagingTemplate messagingTemplate;

    // 편집 세션 시작
    @MessageMapping("/plan.join/{planId}")
    public void joinPlanEditing(@DestinationVariable int planId, @Payload Map<String, Object> payload) {
        int userId = (Integer) payload.get("userId");
        String sessionId = (String) payload.get("sessionId");
        
        PlanEditingSession session = collaborationService.startEditingSession(planId, userId, sessionId);
        
        // 다른 편집자들에게 새 사용자 참여 알림
        PlanChangeEvent event = new PlanChangeEvent(
            "USER_JOIN", planId, userId, session.getUserName(), 
            session, System.currentTimeMillis(), 0
        );
        
        messagingTemplate.convertAndSend("/topic/plan/" + planId, event);
    }

    // 편집 세션 종료
    @MessageMapping("/plan.leave/{planId}")
    public void leavePlanEditing(@DestinationVariable int planId, @Payload Map<String, Object> payload) {
        int userId = (Integer) payload.get("userId");
        String sessionId = (String) payload.get("sessionId");
        
        collaborationService.endEditingSession(planId, userId, sessionId);
        
        // 다른 편집자들에게 사용자 퇴장 알림
        PlanChangeEvent event = new PlanChangeEvent(
            "USER_LEAVE", planId, userId, "", 
            null, System.currentTimeMillis(), 0
        );
        
        messagingTemplate.convertAndSend("/topic/plan/" + planId, event);
    }

    // 실시간 변경사항 브로드캐스트
    @MessageMapping("/plan.change/{planId}")
    public void broadcastChange(@DestinationVariable int planId, @Payload PlanChangeEvent event) {
        // 변경사항을 다른 편집자들에게 전송
        messagingTemplate.convertAndSend("/topic/plan/" + planId, event);
    }

    // 현재 편집 중인 사용자 목록 조회
    @GetMapping("/api/v1/plan/{planId}/editors")
    @ResponseBody
    public List<PlanEditingSession> getCurrentEditors(@PathVariable int planId) {
        return collaborationService.getCurrentEditors(planId);
    }
}
