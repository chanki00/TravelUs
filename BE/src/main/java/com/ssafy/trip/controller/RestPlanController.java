	package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.Itinerary;
import com.DB_PASSWORD_REDACTED.trip.dto.ItineraryPlaceResponseDto;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.service.PlanService;
import com.DB_PASSWORD_REDACTED.trip.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class RestPlanController {
	
	private final PlanService service;
	private final UserService userService;
	
	@PostMapping("")
	public ResponseEntity<?> createPlan(@RequestBody Tripplan dto){
		  try {
		        int res = service.insert(dto);
		        return ResponseEntity.ok(dto.getId());
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insert failed: " + e.getMessage());
		    }
	}
	// 여행 계획 업데이트 API 추가
		@PutMapping("/{planId}")
		public ResponseEntity<?> updatePlan(@PathVariable int planId, @RequestBody Tripplan dto){
			try {
				// planId 설정
				Tripplan updateDto = new Tripplan(
					planId,
					dto.getDestination(),
					dto.getDuration(),
					dto.getMembers(),
					dto.getTransport(),
					dto.getChatroomId(),
					dto.getUserId(),
					dto.getTitle(),
					dto.getDescription(),
					dto.getLikes(),
					dto.getShares(),
					dto.getCreatedAt(),
					dto.getUpdatedAt(),
					dto.getIsShared(),
					dto.getImage()
				);
				
				int res = service.updatePlan(updateDto);
				if (res > 0) {
					return ResponseEntity.ok("Plan updated successfully");
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
				}
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed: " + e.getMessage());
			}
		}
		
		@DeleteMapping("/itinerary/{planId}")
		public ResponseEntity<?> deleteAllItinerary(@PathVariable int planId){
			try {
				int res = service.deleteAllItinerary(planId);
				return ResponseEntity.ok("Itinerary deleted successfully");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete failed: " + e.getMessage());
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
	
	@GetMapping("")
	public ResponseEntity<List<Tripplan>> getAllTripplan(){
		List<Tripplan> tripplans = service.getAllTripplan();
		 return ResponseEntity.ok(tripplans);
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
	    try {
	        int res = service.updateShare(planId);
	        if (res > 0) {
	            // 업데이트된 여행 계획 정보 반환
	            Tripplan updatedPlan = service.getTripplanById(planId);
	            return ResponseEntity.ok(updatedPlan);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
	    }
	}
	
	@GetMapping("/user-info/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable int userId) {
	    try {
	        // UserService를 통해 사용자 정보 조회
	        UserDto user = userService.getUserById(userId);
	        if (user == null) {
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(user);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User fetch failed: " + e.getMessage());
	    }
	}
	
	@DeleteMapping("/{planId}/user/{userId}")
	public ResponseEntity<?> deleteTripplan(@PathVariable int planId, @PathVariable int userId) {
	    try {
	        boolean success = service.deleteTripplan(planId, userId);
	        if (success) {
	            return ResponseEntity.ok().body("여행계획이 성공적으로 삭제되었습니다.");
	        } else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없거나 존재하지 않는 여행계획입니다.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패: " + e.getMessage());
	    }
	}
	
	@DeleteMapping("/{planId}")
	public ResponseEntity<?> deleteTripplanAdmin(@PathVariable int planId) {
	    try {
	        boolean success = service.deleteTripplanAdmin(planId);
	        if (success) {
	            return ResponseEntity.ok().body("여행계획이 성공적으로 삭제되었습니다.");
	        } else {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없거나 존재하지 않는 여행계획입니다.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패: " + e.getMessage());
	    }
	}
	
}
