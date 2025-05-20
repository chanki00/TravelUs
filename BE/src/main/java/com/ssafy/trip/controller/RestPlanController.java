package com.DB_PASSWORD_REDACTED.trip.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.BasicPlanDTO;
import com.DB_PASSWORD_REDACTED.trip.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class RestPlanController {
	
	private final PlanService service;
	
	@PostMapping("")
	public ResponseEntity<?> createPlan(@RequestBody BasicPlanDTO dto){
		  try {
		        int res = service.insert(dto);
		        return ResponseEntity.ok("Insert successful");
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insert failed: " + e.getMessage());
		    }
	}
	
}
