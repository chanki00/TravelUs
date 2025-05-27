package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.Itinerary;
import com.DB_PASSWORD_REDACTED.trip.dto.ItineraryPlaceResponseDto;
import com.DB_PASSWORD_REDACTED.trip.dto.Post;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanService {
	
	private final PlanRepository repo;
	
	public int insert(Tripplan dto) {
		 try {
			 	int res = repo.insert(dto); 
			 	makeDaysPlan(dto);
		        return res; // 예: DB 삽입 시 오류 발생 가능
		    } catch (Exception e) {
		        throw e;
		    }
	}

	public int updatePlan(Tripplan dto) {
		try {
			return repo.updatePlan(dto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int deleteAllItinerary(int planId) {
		try {
			return repo.deleteAllItinerary(planId);
		} catch (Exception e) {
			throw e;
		}
	}
	public void makeDaysPlan(Tripplan dto) {
		for (int day = 1; day <= dto.getDuration() ; day++) {
			repo.insertDay(dto.getId(), day);
		}
	}

	public Tripplan getTripplanById(int planId) {
        return repo.selectTripplanById(planId);
    }

	public int getPlanDaysId(int planId, int dayNumber) {
		return repo.getPlanDaysId(planId, dayNumber);
	}

	public int insertItinerary(Itinerary req) {
		return repo.insertItinerary(req);
	}

	public List<Tripplan> getTripplanByUserId(int userId) {
		return repo.getTripplanByUserId(userId);
	}

	public int updateShare(int planId) {
		return repo.updateShare(planId);
	}

	public List<Tripplan> getAllSharePlan() {
		return repo.getAllSharePlan();
	}

	public List<ItineraryPlaceResponseDto> getItineraryByPlanId(int planId) {
		return repo.getItineraryByPlanId(planId);
	}

	public int copyTripPlan(Post req, int originalPlanId, int newUserId, int chatroomid) {
    try {
        // 1. 원본 여행계획 조회
        Tripplan originalPlan = repo.selectTripplanById(originalPlanId);
        if (originalPlan == null) {
            throw new RuntimeException("원본 여행계획을 찾을 수 없습니다.");
        }
        
        // 2. 새로운 여행계획 생성 (복사본)
        Tripplan newPlan = new Tripplan(
            0, // id는 자동 생성
            originalPlan.getDestination(),
            originalPlan.getDuration(),
            originalPlan.getMembers(),
            originalPlan.getTransport(),
            chatroomid,
            newUserId, // 새로운 사용자 ID
            req.getTitle(),
            originalPlan.getDescription(),
            0, // likes 초기화
            0, // shares 초기화
            null, // createdAt은 DB에서 자동 설정
            null, // updatedAt은 DB에서 자동 설정
            0, // isShared 초기화
            originalPlan.getImage()
        );
        
        // 3. 새로운 여행계획 저장
        int result = repo.insert(newPlan);
        if (result <= 0) {
            throw new RuntimeException("새로운 여행계획 생성에 실패했습니다.");
        }
        
        // 4. 일차별 계획 생성
        makeDaysPlan(newPlan);
        
        // 5. 원본의 여행 일정 복사
        copyItineraryFromOriginal(originalPlanId, newPlan.getId());
        
        return newPlan.getId();
        
    } catch (Exception e) {
        throw new RuntimeException("여행계획 복사 중 오류가 발생했습니다: " + e.getMessage(), e);
    }
}

private void copyItineraryFromOriginal(int originalPlanId, int newPlanId) {
    try {
        // 원본 여행계획의 모든 일정 조회
        List<ItineraryPlaceResponseDto> originalItinerary = repo.getItineraryByPlanId(originalPlanId);
        
        for (ItineraryPlaceResponseDto item : originalItinerary) {
            // 새로운 계획의 해당 일차 ID 조회
            int newDayId = repo.getPlanDaysId(newPlanId, item.getDayNumber());
            
            // 새로운 일정 항목 생성
            Itinerary newItinerary = new Itinerary(
                newDayId,
                item.getAttractionId(),
                item.getPlaceOrder(),
                item.getMemo()
            );
            
            // 새로운 일정 저장
            repo.insertItinerary(newItinerary);
        }
    } catch (Exception e) {
        throw new RuntimeException("여행 일정 복사 중 오류가 발생했습니다: " + e.getMessage(), e);
    }
}

public boolean deleteTripplan(int planId, int userId) {
    try {
        // 1. 파라미터 맵 생성
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        params.put("userId", userId);
        
        // 2. 계획 소유권 확인
        int ownershipCount = repo.checkPlanOwnership(params);
        if (ownershipCount == 0) {
            return false; // 권한 없음 또는 존재하지 않음
        }
        
        // 3. CASCADE를 활용한 단일 쿼리 삭제
        // TripPlans 삭제 시 관련된 모든 데이터가 자동으로 삭제됨
        // - TripPlanDays (ON DELETE CASCADE)
        // - TripPlanPlaces (ON DELETE CASCADE)
        // - TripPlanTags (ON DELETE CASCADE)
        // - TripPlanLikes (ON DELETE CASCADE)
        int result = repo.deleteTripPlan(params);
        
        return result > 0;
    } catch (Exception e) {
        throw new RuntimeException("여행계획 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
    }
}

public List<Tripplan> getAllTripplan() {
	return repo.getAllTripplan();
}

public boolean deleteTripplanAdmin(int planId) {
	int result = repo.deleteTripplanAdmin(planId);
	return result > 0;
}

public Tripplan getTripplanByChatroomId(Integer chatroomId) {
	return repo.getTripplanByChatroomId(chatroomId);
}
}
