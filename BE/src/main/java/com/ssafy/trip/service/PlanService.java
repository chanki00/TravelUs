package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
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

	public int insertItinerary(int dayId, int attractionId, int order) {
		return repo.insertItinerary(dayId, attractionId, order);
	}

}
