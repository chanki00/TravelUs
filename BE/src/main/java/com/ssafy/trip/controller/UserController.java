package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService uService;
	
	@GetMapping("/{id}")
	@Operation(summary = "id 검색 사용자 조회")
	public ResponseEntity<UserDto> selectUser(@PathVariable Long id) {
		UserDto user = uService.selectUser(id);
		
//		Map<String, Object> map = new HashMap<>();
//		map.put("message", "사용자 조회: " + user);
//		
//		ResponseEntity<Map<String, Object>> res = new ResponseEntity<>(map, HttpStatus.OK);
		
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("")
	@Operation(summary = "모든 사용자 정보 조회")
	public ResponseEntity<List<UserDto>> selectAllUser() {
		
		
		
		return null;
	}
}
