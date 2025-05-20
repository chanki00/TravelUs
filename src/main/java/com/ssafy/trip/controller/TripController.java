package com.DB_PASSWORD_REDACTED.trip.controller;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.Guguns;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.Sidos;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripContentDto;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripPost;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentData;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentTypes;
import com.DB_PASSWORD_REDACTED.trip.service.TripService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//@WebServlet("/trip")
@Controller
@RequestMapping("/trip")
@RequiredArgsConstructor
public class TripController {
	
	private final TripService service;

	@GetMapping("mytripplan")
	private String myPlan(Model model) throws ClassNotFoundException, SQLException {
	    List<Sidos> sidos = service.ContentData();
	    
	    // 시도별 시군구 정보를 맵으로 변환 (예: key: sidoCode, value: guguns 목록)
	    Map<Integer, List<Guguns>> gunMap = new HashMap<>();
	    for (Sidos sido : sidos) {

	        gunMap.put(sido.getSidoCode(), sido.getGuguns());
	    }
	    model.addAttribute("sidos", sidos);
	    model.addAttribute("gunMap", gunMap);  // 시군구 데이터를 JSP로 전달
	    List<contentTypes> contenttypes = service.getContentTypes();
	    model.addAttribute("contents",contenttypes);
		return "trip/mytripplan";
	}

	@GetMapping("list")
	private String listPosts(@RequestParam(required = false) String searchField,
				@RequestParam(required = false) String searchKeyword,
				@RequestParam(required = false) String pageParam,
				Model model) throws Exception {
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

	    totalPages = (int) Math.ceil((double) totalCount / size);

	    model.addAttribute("postList", postList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("searchField", searchField);
	    model.addAttribute("searchKeyword", searchKeyword);

	    return "trip/sharetrip";
	}

	@GetMapping("localtripPage")
	private String registTripContent(Model model) throws Exception {
	    List<Sidos> sidos = service.ContentData();
	    
	    // 시도별 시군구 정보를 맵으로 변환 (예: key: sidoCode, value: guguns 목록)
	    Map<Integer, List<Guguns>> gunMap = new HashMap<>();
	    for (Sidos sido : sidos) {

	        gunMap.put(sido.getSidoCode(), sido.getGuguns());
	    }
	    model.addAttribute("sidos", sidos);
	    model.addAttribute("gunMap", gunMap);  // 시군구 데이터를 JSP로 전달
	    List<contentTypes> contenttypes = service.getContentTypes();
	    model.addAttribute("contents",contenttypes);
		return "trip/localtrip";
	}

	@PostMapping("localtripSearch")
	@ResponseBody
	private List<contentData> searchContentXY(
            @RequestParam String sido,
            @RequestParam String gugun,
            @RequestParam("contentType") String content) throws ClassNotFoundException, SQLException {

		return  service.contentSearch(sido, gugun, content);

		
	}
	
	
}
