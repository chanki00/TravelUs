package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.TripPlanLike;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;

@Mapper
public interface TripPlanLikeRepository {
    
    // 좋아요 추가
    int insertLike(TripPlanLike like);
    
    // 좋아요 삭제
    int deleteLike(int planId, int userId);
    
    // 사용자가 특정 계획에 좋아요를 눌렀는지 확인
    int checkUserLike(int planId, int userId);
    
    // 특정 계획의 좋아요 수 조회
    int getLikeCount(int planId);
    
    // 좋아요 수가 많은 여행 계획 조회 (상위 N개)
    List<Tripplan> getTopLikedPlans(int limit);
    
    // 여행 계획의 좋아요 수 업데이트
    int updatePlanLikeCount(int planId, int likeCount);
}
