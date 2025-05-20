package com.DB_PASSWORD_REDACTED.trip.controller.rest;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.trip.Sidos;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripPost;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentTypes;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.ContentSearchRequest;
import com.DB_PASSWORD_REDACTED.trip.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trip")
@RequiredArgsConstructor
@Tag(name = "RestTripController", description = "여행 관련 API 제공")
public class RestTripController {

	private final TripService service;

	@GetMapping("/sido")
	@Operation(summary = "시도, 구군 정보 조회")
	public ResponseEntity<?> getSido() throws ClassNotFoundException, SQLException{
		List<Sidos> sidos = service.ContentData();
		
		return ResponseEntity.ok(sidos);
	}
	
	@GetMapping("/contenttype")
	@Operation(summary = "콘텐츠 타입 정보 조회")
	public ResponseEntity<?> getContentType(){
	    List<contentTypes> contenttypes = service.getContentTypes();
		return ResponseEntity.ok(contenttypes);
	}
	
	@PostMapping("/content/search")
	@Operation(summary = "시도, 구군, 콘텐츠 타입 검색 조회")
	public ResponseEntity<?> searchbyContent(@RequestBody ContentSearchRequest search){
		return ResponseEntity.ok(service.contentSearch(search.getSido(), search.getGugun(), search.getContent()));
	}
	
	@GetMapping("/keyword/search")
	@Operation(summary = "검색어 조회")
	public ResponseEntity<?> searchbyKeyword(@RequestParam String searchField,
											@RequestParam String searchKeyword,
											@RequestParam String pageParam) throws Exception{
		int page = 1;
	    int size = 5;

	    if (pageParam != null && !pageParam.isBlank()) {
	        page = Integer.parseInt(pageParam);
	    }

	    int offset = (page - 1) * size;

	    List<TripPost> postList;
	    int totalCount;
	    int totalPages;

	    if (searchField != null && searchKeyword != null && !searchKeyword.isBlank()) {
	        postList = service.searchPosts(searchField, searchKeyword, offset, size);
	        totalCount = service.countSearchPosts(searchField, searchKeyword);
	    } else {
	        postList = service.selectPostList(offset, size);
	        totalCount = service.countPosts();
	    }
	    return ResponseEntity.ok(postList);
	}
	
}
