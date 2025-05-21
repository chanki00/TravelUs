package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;

@Mapper
public interface PlanRepository {

	int insert(Tripplan dto);

	void insertDay(int plan_id, int day_number);

	Tripplan selectTripplanById(int planId);

	int getPlanDaysId(int planId, int dayNumber);

	int insertItinerary(int dayId, int attractionId, int order);

	List<Tripplan> getTripplanByUserId(int userId);

	int updateShare(int planId);

	List<Tripplan> getAllSharePlan();
}
