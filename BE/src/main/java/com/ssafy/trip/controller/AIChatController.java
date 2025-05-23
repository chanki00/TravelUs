package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.Attraction;
import com.DB_PASSWORD_REDACTED.trip.service.AiChatService;
import com.DB_PASSWORD_REDACTED.trip.service.TripService;
import com.DB_PASSWORD_REDACTED.trip.service.TripServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AIChatController {
	private final AiChatService chatService;
	private final TripServiceImpl tripService;
	
	@PostMapping("/simple")
    ResponseEntity<?> simpleGeneration(@RequestBody Map<String, String> userInput) {
        Object result = chatService.simpleGeneration(userInput.get("message"));
        
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", result
            ));
    }
	
	@PostMapping("/trip-plan")
    public ResponseEntity<?> generateTripPlan(@RequestBody Map<String, Object> tripRequest) {
        log.info("여행 계획 생성 요청: {}", tripRequest);
        
        // 여행지 코드 가져오기
        String destinationName = tripRequest.get("destination").toString();
        int sidoCode = getSidoCodeFromName(destinationName);
        
        // 해당 지역의 관광지 목록 가져오기
        List<Attraction> attractions = tripService.getAttractionBysidoCode(sidoCode);
        
        if (attractions == null || attractions.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                "status", "ERROR",
                "message", "해당 지역에 등록된 관광지가 없습니다."
            ));
        }
        
        log.info("관광지 목록 조회 완료: {} 개", attractions.size());
        
        // 여행 계획 생성을 위한 프롬프트 구성
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 조건에 맞는 여행 계획을 만들어주세요:\n");
        
        if (tripRequest.containsKey("destination")) {
            prompt.append("- 여행지: ").append(tripRequest.get("destination")).append("\n");
        }
        
        if (tripRequest.containsKey("duration")) {
            prompt.append("- 기간: ").append(tripRequest.get("duration")).append("박 ")
                  .append(Integer.parseInt(tripRequest.get("duration").toString()) + 1).append("일\n");
        }
        
        if (tripRequest.containsKey("style")) {
            Object styleObj = tripRequest.get("style");
            if (styleObj instanceof List) {
                List<String> styles = (List<String>) styleObj;
                prompt.append("- 여행 스타일: ").append(String.join(", ", styles)).append("\n");
            } else {
                prompt.append("- 여행 스타일: ").append(styleObj).append("\n");
            }
        }
        
        if (tripRequest.containsKey("companions")) {
            prompt.append("- 동행자: ").append(tripRequest.get("companions")).append("\n");
        }
        
        if (tripRequest.containsKey("budget")) {
            prompt.append("- 예산: ").append(tripRequest.get("budget")).append("\n");
        }
        
        // 관광지 목록 추가 (최대 30개까지만 포함)
        prompt.append("\n다음은 해당 지역에 있는 실제 관광지 목록입니다. 반드시 이 목록에서만 장소를 선택해주세요:\n");
        
        int maxAttractions = Math.min(attractions.size(), 30);
        for (int i = 0; i < maxAttractions; i++) {
            Attraction attraction = attractions.get(i);
            prompt.append(i + 1).append(". ID: ").append(attraction.getNo())
                  .append(", 이름: ").append(attraction.getTitle())
                  .append(", 주소: ").append(attraction.getAddr())
                  .append(", 유형: ").append(getContentTypeName(attraction.getContentTypeId()))
                  .append("\n");
        }
        
        prompt.append("\n다음 형식으로 응답해주세요:\n");
        prompt.append("1. 여행 계획 개요 (제목, 설명)\n");
        prompt.append("2. 일차별 일정 (각 일차마다 방문할 장소 2-3곳, 각 장소에 대한 간략한 설명)\n");
        prompt.append("3. 추천 맛집\n");
        prompt.append("4. 여행 팁\n\n");
        prompt.append("JSON 형식으로 다음 구조에 맞게 응답해주세요:\n");
        prompt.append("{\n");
        prompt.append("  \"title\": \"여행 제목\",\n");
        prompt.append("  \"description\": \"여행 설명\",\n");
        prompt.append("  \"itinerary\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"day\": 1,\n");
        prompt.append("      \"places\": [\n");
        prompt.append("        {\n");
        prompt.append("          \"id\": 관광지ID(숫자),\n");
        prompt.append("          \"name\": \"장소명\",\n");
        prompt.append("          \"description\": \"장소 설명\",\n");
        prompt.append("          \"type\": \"관광/식당/숙소\"\n");
        prompt.append("        }\n");
        prompt.append("      ]\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"restaurants\": [\"추천 맛집1\", \"추천 맛집2\"],\n");
        prompt.append("  \"tips\": [\"여행 팁1\", \"여행 팁 2\"]\n");
        prompt.append("}\n");
        prompt.append("\n중요: 반드시 위에 제공된 관광지 목록에서만 장소를 선택하고, 각 장소의 ID를 정확히 포함해야 합니다.\n");
        
        log.info("AI 프롬프트 생성 완료");
        
        // AI 서비스 호출
        Object result = chatService.simpleGeneration(prompt.toString());
        log.info("AI 여행 계획 응답 완료");
        
        // 응답 처리
        try {
            // AI 응답에서 JSON 부분 추출 시도
            String resultStr = result.toString();
            int jsonStart = resultStr.indexOf("{");
            int jsonEnd = resultStr.lastIndexOf("}") + 1;
            
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                String jsonStr = resultStr.substring(jsonStart, jsonEnd);
                // JSON 파싱 시도
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object parsedJson = mapper.readValue(jsonStr, Object.class);
                    
                    // 관광지 정보 맵 생성 (ID로 빠르게 조회하기 위함)
                    Map<Integer, Attraction> attractionMap = attractions.stream()
                        .collect(Collectors.toMap(Attraction::getNo, a -> a));
                    
                    // 파싱된 JSON에 관광지 상세 정보 추가 처리 로직을 여기에 추가할 수 있음
                    
                    return ResponseEntity.ok(Map.of(
                        "status", "SUCCESS",
                        "plan", parsedJson,
                        "attractions", attractionMap
                    ));
                } catch (Exception e) {
                    log.error("JSON 파싱 오류: {}", e.getMessage());
                }
            }
            
            // JSON 파싱에 실패한 경우 원본 텍스트 반환
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", result,
                "rawResponse", true
            ));
        } catch (Exception e) {
            log.error("AI 응답 처리 오류: {}", e.getMessage());
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", result
            ));
        }
    }
    
    // 여행지 이름으로 시도 코드 가져오기
    private int getSidoCodeFromName(String destinationName) {
    	Map<String, Integer> sidoMap = Map.ofEntries(
    		    Map.entry("서울", 1),
    		    Map.entry("인천", 2),
    		    Map.entry("대전", 3),
    		    Map.entry("대구", 4),
    		    Map.entry("광주", 5),
    		    Map.entry("부산", 6),
    		    Map.entry("울산", 7),
    		    Map.entry("세종", 8),
    		    Map.entry("경기도", 31),
    		    Map.entry("강원도", 32),
    		    Map.entry("충청북도", 33),
    		    Map.entry("충청남도", 34),
    		    Map.entry("경상북도", 35),
    		    Map.entry("경상남도", 36),
    		    Map.entry("전라북도", 37),
    		    Map.entry("전라남도", 38),
    		    Map.entry("제주도", 39)
    		);
        
        return sidoMap.getOrDefault(destinationName, 1); // 기본값은 서울(1)
    }
    
    // 컨텐츠 타입 ID를 이름으로 변환
    private String getContentTypeName(int contentTypeId) {
        switch (contentTypeId) {
            case 12: return "관광지";
            case 14: return "문화시설";
            case 15: return "축제/공연/행사";
            case 25: return "여행코스";
            case 28: return "레포츠";
            case 32: return "숙박";
            case 38: return "쇼핑";
            case 39: return "음식점";
            default: return "기타";
        }
    }
}
