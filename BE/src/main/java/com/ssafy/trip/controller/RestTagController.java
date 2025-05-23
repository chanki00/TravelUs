package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.TagDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.UserTagUpdateRequest;
import com.DB_PASSWORD_REDACTED.trip.service.TagServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class RestTagController {

	private final TagServiceImpl service;
	
	@GetMapping("/trip")
	public ResponseEntity<List<TagDTO>> getTripTags() {
		return ResponseEntity.ok(service.getTripTags());
    }
	
	@PostMapping("/tripplan/{planId}/{tagId}")
	public ResponseEntity<?> insertTripplantags(@PathVariable int planId, @PathVariable int tagId){
		return ResponseEntity.ok(service.insertplanTag(planId, tagId));
	}
	
	@GetMapping("/plan/{planId}")
	public ResponseEntity<List<String>> getTripplanTagsName(@PathVariable int planId) {
		return ResponseEntity.ok(service.getTripplanTagsName(planId));
    }
	
	@GetMapping("/user")
	public ResponseEntity<List<TagDTO>> getUserTags() {
		return ResponseEntity.ok(service.getUserTags());
	}
	
	@PostMapping("/user/{userId}/{tagId}")
	public ResponseEntity<?> insertUserTag(@PathVariable int userId, @PathVariable int tagId) {
		return ResponseEntity.ok(service.insertUserTag(userId, tagId));
	}
	
	@GetMapping("/user/personal/{userId}")
	public ResponseEntity<List<String>> getUserPersonalTagsName(@PathVariable int userId) {
		return ResponseEntity.ok(service.getUserPersonalTagsName(userId));
	}
	
	@GetMapping("/user/trip/{userId}")
	public ResponseEntity<List<String>> getUserTripTagsName(@PathVariable int userId) {
		return ResponseEntity.ok(service.getUserTripTagsName(userId));
	}
	
	@PatchMapping("/user")
	public ResponseEntity<?> updateUserTags(@RequestBody UserTagUpdateRequest dto) {
		
		String type;
		if (dto.getType().equals("personal")) {
			type = "성격";
		}
		else {
			type = "여행";
		}
		
	    service.updateUserTags(dto.getUserId(), type, dto.getTagIds());
	    return ResponseEntity.ok().build();
	}

}