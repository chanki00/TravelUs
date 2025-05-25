package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.service.TripPlanLikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class TripPlanLikeController {
    
    private final TripPlanLikeService likeService;
    
    // 좋아요 토글
    @PostMapping("/toggle/{planId}")
    public ResponseEntity<?> toggleLike(@PathVariable int planId, @RequestParam int userId) {
        try {
            boolean isLiked = likeService.toggleLike(planId, userId);
            int likeCount = likeService.getLikeCount(planId);
            
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "isLiked", isLiked,
                "likeCount", likeCount
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "ERROR",
                "message", e.getMessage()
            ));
        }
    }
    
    // 사용자 좋아요 상태 확인
    @GetMapping("/check/{planId}")
    public ResponseEntity<?> checkUserLike(@PathVariable int planId, @RequestParam int userId) {
        boolean isLiked = likeService.isUserLiked(planId, userId);
        int likeCount = likeService.getLikeCount(planId);
        
        return ResponseEntity.ok(Map.of(
            "isLiked", isLiked,
            "likeCount", likeCount
        ));
    }
    
    // 인기 여행 계획 조회
    @GetMapping("/popular")
    public ResponseEntity<List<Tripplan>> getPopularPlans(@RequestParam int limit) {
        List<Tripplan> popularPlans = likeService.getTopLikedPlans(limit);
        return ResponseEntity.ok(popularPlans);
    }
}
