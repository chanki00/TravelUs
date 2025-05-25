package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.Itinerary;
import com.DB_PASSWORD_REDACTED.trip.dto.ItineraryPlaceResponseDto;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;

@Mapper
public interface PlanRepository {

	int insert(Tripplan dto);

	void insertDay(int plan_id, int day_number);

	int updatePlan(Tripplan dto);

	int deleteAllItinerary(int planId);

	Tripplan selectTripplanById(int planId);

	int getPlanDaysId(int planId, int dayNumber);

	int insertItinerary(Itinerary itinerary);

	List<Tripplan> getTripplanByUserId(int userId);

	int updateShare(int planId);

	List<Tripplan> getAllSharePlan();

	List<ItineraryPlaceResponseDto> getItineraryByPlanId(int planId);

	// CASCADE를 활용한 단순화된 삭제
	int deleteTripPlan(Map<String, Object> params);
	
	// 계획 소유권 확인
	int checkPlanOwnership(Map<String, Object> params);

	List<Tripplan> getAllTripplan();

	int deleteTripplanAdmin(int planId);

	Tripplan getTripplanByChatroomId(Integer chatroomId);
}
