package com.DB_PASSWORD_REDACTED.trip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class RestPlanController {
	
	private final PlanService service;
	
	@PostMapping("")
	public ResponseEntity<?> createPlan(@RequestBody Tripplan dto){
		  try {
		        int res = service.insert(dto);
		        return ResponseEntity.ok(dto.getId());
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insert failed: " + e.getMessage());
		    }
	}
	
	@GetMapping("/{planId}")
	 public ResponseEntity<Tripplan> getTripplan(@PathVariable int planId) {
        Tripplan tripplan = service.getTripplanById(planId);
        if (tripplan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tripplan);
    }
	
	
	@GetMapping("/tripplandays/{planId}/{dayNumber}")
	public ResponseEntity<?> getPlanDaysID(@PathVariable int planId, @PathVariable int dayNumber){
		return ResponseEntity.ok(service.getPlanDaysId(planId, dayNumber));
	}
	
	@PostMapping("/itinerary/{dayId}/{attractionId}/{order}")
	public ResponseEntity<?> createItinerary(
			@PathVariable int dayId
			, @PathVariable int attractionId
			, @PathVariable int order){
		return ResponseEntity.ok(service.insertItinerary(dayId, attractionId,order));
	}
	
	
}
