package com.DB_PASSWORD_REDACTED.trip.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;
import com.DB_PASSWORD_REDACTED.trip.dto.MemberInfoDto;
import com.DB_PASSWORD_REDACTED.trip.dto.MemberPwDto;
import com.DB_PASSWORD_REDACTED.trip.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestMemberController {
	private final MemberService mService;
	
	@GetMapping("/member/{id}")
	@Operation(summary = "id 검색 멤버 조회")
	public ResponseEntity<Map<String, Object>> selectMember(@PathVariable String id) throws Exception {
		MemberDto member = mService.selectMember(id);
		
		Map<String, Object> map = new HashMap<>();
		map.put("message", "멤버 조회: " + member);
		
		ResponseEntity<Map<String, Object>> res = new ResponseEntity<>(map, HttpStatus.OK);
		
		return res;
	}
	
	@GetMapping("/member")
	@Operation(summary = "모든 멤버 정보 조회")
	public ResponseEntity<Map<String, Object>> selectAllMember() throws Exception {
		
		List<MemberDto> memberList = mService.selectAllMember();
		
		Map<String, Object> map = new HashMap<>();
		map.put("message", "조회완료");
		map.put("data", memberList);
		
		ResponseEntity<Map<String, Object>> res = new ResponseEntity<>(map, HttpStatus.OK);
		
		return res;
	}
	
	@PostMapping("/member")
	@Operation(summary = "멤버 삽입")
	public ResponseEntity<Map<String, Object>> insertMember(@RequestBody MemberDto member) throws Exception {
		
		int cnt = mService.insertMember(member);
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "등록 실패");
			return ResponseEntity.status(500).body(map);
		}
		
		map.put("message", "멤버 등록: " + member.getId());
		return ResponseEntity.ok(map);
	}
	
	@PatchMapping("/member/info")
	@Operation(summary = "멤버 수정")
	public ResponseEntity<Map<String, Object>> updateInfo(@RequestBody MemberInfoDto memberInfo) throws Exception {
		
		int cnt = mService.updateInfo(memberInfo.getId(), memberInfo.getName(), memberInfo.getEmail());
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "수정 실패");
			return ResponseEntity.status(500).body(map);
		}
		
		map.put("message", "멤버 정보 수정: " + memberInfo);
		return ResponseEntity.ok(map);
	}
	
	@PatchMapping("/member/pw")
	@Operation(summary = "비밀번호 수정")
	public ResponseEntity<Map<String, Object>> updatePw(@RequestBody MemberPwDto memberPw) throws Exception {
		
		int cnt = mService.updatePw(memberPw.getId(), memberPw.getPw());
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "수정 실패");
			return ResponseEntity.status(500).body(map);
		}
		
		map.put("message", "비밀번호 수정: " + memberPw.getId());
		return ResponseEntity.ok(map);
	}
	
	@DeleteMapping("/member/{id}")
	@Operation(summary = "멤버 삭제")
	public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable String id) throws Exception {
		
		int cnt = mService.deleteMember(id);
		
		Map<String, Object> map = new HashMap<>();
		
		if (cnt == 0) {
			map.put("message", "삭제 실패");
			return ResponseEntity.status(500).body(map);
		}
		
		map.put("message", "멤버 삭제: " + id);
		return ResponseEntity.ok(map);
	}
	
	
	
	
}
