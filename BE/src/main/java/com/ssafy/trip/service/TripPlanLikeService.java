package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.TripPlanLike;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.repository.TripPlanLikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripPlanLikeService {
    
    private final TripPlanLikeRepository repository;
    
    // 좋아요 토글 (좋아요가 있으면 삭제, 없으면 추가)
    public boolean toggleLike(int planId, int userId) {
        boolean isLiked = repository.checkUserLike(planId, userId) > 0;
        
        if (isLiked) {
            // 좋아요 삭제
            repository.deleteLike(planId, userId);
        } else {
            // 좋아요 추가
            TripPlanLike like = new TripPlanLike();
            like = new TripPlanLike(0, planId, userId, null);
            repository.insertLike(like);
        }
        
        // 좋아요 수 업데이트
        int likeCount = repository.getLikeCount(planId);
        repository.updatePlanLikeCount(planId, likeCount);
        
        return !isLiked; // 새로운 좋아요 상태 반환
    }
    
    // 사용자가 특정 계획에 좋아요를 눌렀는지 확인
    public boolean isUserLiked(int planId, int userId) {
        return repository.checkUserLike(planId, userId) > 0;
    }
    
    // 특정 계획의 좋아요 수 조회
    public int getLikeCount(int planId) {
        return repository.getLikeCount(planId);
    }
    
    // 인기 여행 계획 조회
    public List<Tripplan> getTopLikedPlans(int limit) {
        return repository.getTopLikedPlans(limit);
    }
}
