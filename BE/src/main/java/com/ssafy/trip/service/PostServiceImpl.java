package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.Post;
import com.DB_PASSWORD_REDACTED.trip.repository.PostRepository;
import com.DB_PASSWORD_REDACTED.trip.repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository repo;
    private final PlanRepository planRepo; // 추가
    private final PlanService planService; // 추가

    public int createRecruitPost(Post req) {
        try {
            // 1. 선택한 여행계획이 있는 경우 복사본 생성
            if (req.getPlanId() != null && req.getPlanId() > 0) {
                int newPlanId = planService.copyTripPlan(req.getPlanId(), req.getUserId());
                
                // 새로운 planId로 변경
                Post newPost = new Post(
                    req.getId(),
                    newPlanId, // 복사된 새로운 planId 사용
                    req.getUserId(),
                    req.getStartDate(),
                    req.getEndDate(),
                    req.getTitle(),
                    req.getContent(),
                    req.getCurrentMembers(),
                    req.getCreatedAt(),
                    req.getUpdatedAt(),
                    req.getPersonalityTags()
                );
                
                return repo.createRecruitPost(newPost);
            } else {
                // 여행계획이 없는 경우 그대로 생성
                return repo.createRecruitPost(req);
            }
        } catch (Exception e) {
            throw new RuntimeException("모집글 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public List<Post> getALlRecruitPost() {
        return repo.getALlRecruitPost();
    }
    @Override
    public int deleteRecruitPost(int postId) {
        try {
            return repo.deleteRecruitPost(postId);
        } catch (Exception e) {
            throw new RuntimeException("모집글 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
