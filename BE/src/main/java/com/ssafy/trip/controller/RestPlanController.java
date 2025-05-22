package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

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
import com.DB_PASSWORD_REDACTED.trip.dto.Itinerary;
import com.DB_PASSWORD_REDACTED.trip.dto.ItineraryPlaceResponseDto;
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
	
	@GetMapping("/itinerary/{planId}")
	 public ResponseEntity<List<ItineraryPlaceResponseDto>> getItineraryByplanId(@PathVariable int planId) {
		List<ItineraryPlaceResponseDto> list = service.getItineraryByPlanId(planId);

       return ResponseEntity.ok(list);
   }
	
	@GetMapping("/user/{userId}")
	 public ResponseEntity<List<Tripplan>> getTripplanByUserId(@PathVariable int userId) {
       List<Tripplan> tripplan = service.getTripplanByUserId(userId);
       if (tripplan == null) {
           return ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(tripplan);
   }
	
	
	@GetMapping("/tripplandays/{planId}/{dayNumber}")
	public ResponseEntity<?> getPlanDaysID(@PathVariable int planId, @PathVariable int dayNumber){
		return ResponseEntity.ok(service.getPlanDaysId(planId, dayNumber));
	}
	
	@PostMapping("/itinerary")
	public ResponseEntity<?> createItinerary(@RequestBody Itinerary req){
		return ResponseEntity.ok(service.insertItinerary(req));
	}
	
	@GetMapping("/share")
	public ResponseEntity<?> getAllSharePlan(){
		List<Tripplan> res = service.getAllSharePlan();
		return ResponseEntity.ok(res);
	}
	
	@PatchMapping("/updateShare/{planId}")
	public ResponseEntity<?> updateShare(@PathVariable int planId){
		int res = service.updateShare(planId);
		return ResponseEntity.ok(res);
	}
	
}
