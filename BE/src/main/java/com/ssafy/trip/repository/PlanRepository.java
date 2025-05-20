package com.DB_PASSWORD_REDACTED.trip.repository;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;

@Mapper
public interface PlanRepository {

	int insert(BasicPlanDTO dto);

	void insertDay(int plan_id, int day_number);

	Tripplan selectTripplanById(int planId);
}
