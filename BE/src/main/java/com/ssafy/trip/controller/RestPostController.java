package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.Itinerary;
import com.DB_PASSWORD_REDACTED.trip.dto.Post;
import com.DB_PASSWORD_REDACTED.trip.service.PostServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class RestPostController {
	
	private final PostServiceImpl service;
	
	@PostMapping("/recruit")
	public ResponseEntity<?> createRecruitPost(@RequestBody Post req){
		return ResponseEntity.ok(service.createRecruitPost(req));
	}
	
	@GetMapping("/recruit")
	public ResponseEntity<List<Post>> getALlRecruitPost(){
		return ResponseEntity.ok(service.getALlRecruitPost());
	}
}
