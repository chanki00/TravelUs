package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.TagDTO;
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
}
