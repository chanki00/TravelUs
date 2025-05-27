package com.DB_PASSWORD_REDACTED.trip.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserInfoDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserPwDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserResponseDto;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;
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
	public ResponseEntity<UserDto> selectUser(@PathVariable int id) throws SQLException {
		UserDto user = uService.selectUser(id);
		
//		Map<String, Object> map = new HashMap<>();
//		map.put("message", "사용자 조회: " + user);
//		
//		ResponseEntity<Map<String, Object>> res = new ResponseEntity<>(map, HttpStatus.OK);
		
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("")
	@Operation(summary = "모든 사용자 정보 조회")
	public ResponseEntity<List<UserDto>> selectAllUser() throws SQLException {
		
		List<UserDto> userList = uService.selectAllUser();
				
		return ResponseEntity.ok(userList);
	}
	
	@PostMapping("")
	@Operation(summary = "멤버 삽입")
	public ResponseEntity<Map<String, Object>> insertUser(@RequestBody UserDto user) throws SQLException {
		
	    int cnt = uService.insertUser(user);

	    Map<String, Object> map = new HashMap<>();
	    
	    if (cnt == 0) {
	    	map.put("message", "등록 실패");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
	    }
	    
	    map.put("message", "등록 성공");
	    map.put("userId", user.getId());
	    
	    return ResponseEntity.status(HttpStatus.CREATED).body(map);
	}
	
	@PatchMapping("/info/{id}")
	@Operation(summary = "멤버 수정")
	public ResponseEntity<Map<String, Object>> updateInfo(@PathVariable int id, @RequestBody UserInfoDto user) throws SQLException {
		user.setId(id);
		
		int cnt = uService.updateInfo(user);
		
		System.out.println(user);
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "멤버 정보 수정 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		
	    map.put("message", "멤버 정보 수정 성공");
	    map.put("userId", user.getId());
		
	    return ResponseEntity.ok(map);
	}
	
	@PatchMapping("/pw/{id}")
	@Operation(summary = "비밀번호 수정")
	public ResponseEntity<Map<String, Object>> updatePw(@PathVariable Long id, @RequestBody UserPwDto user) throws SQLException {
		user.setId(id);
		
		int cnt = uService.updatePw(user);
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "수정 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		
	    map.put("message", "비밀번호 수정 성공");
	    map.put("userId", user.getId());
		
	    return ResponseEntity.ok(map);
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "멤버 삭제")
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id) throws SQLException {
		
		int cnt = uService.deleteUser(id);
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "삭제 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		
		map.put("message", "삭제 성공");
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    UserDto userDto = userDetails.getUser();

	    UserResponseDto user = new UserResponseDto(
	        userDto.getId(), userDto.getUserId(), userDto.getUserEmail(),
	        userDto.getName(), userDto.getAge(), userDto.getGender(),
	        userDto.getAddress(), userDto.getIntro(),
	        userDto.getRole(), userDto.getAllowInvite()
	    );

	    return ResponseEntity.ok(user);
	}
	
}
