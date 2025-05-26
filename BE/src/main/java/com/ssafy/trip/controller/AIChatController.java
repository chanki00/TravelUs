package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;

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
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/ai")
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
       
       // 관광지를 거리 기반으로 그룹화 (효율적인 동선을 위해)
       List<List<Attraction>> clusteredAttractions = clusterAttractionsByDistance(attractions);
       
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
       
       // 효율적인 동선을 위한 지역별 그룹 정보 제공
       prompt.append("\n다음은 효율적인 이동을 위해 지역별로 그룹화된 관광지 목록입니다:\n");
       
       for (int groupIndex = 0; groupIndex < clusteredAttractions.size() && groupIndex < 5; groupIndex++) {
           List<Attraction> group = clusteredAttractions.get(groupIndex);
           prompt.append("\n[지역 그룹 ").append(groupIndex + 1).append(" - 서로 가까운 장소들]\n");
           
           for (int i = 0; i < Math.min(group.size(), 8); i++) {
               Attraction attraction = group.get(i);
               prompt.append("- ID: ").append(attraction.getNo())
                     .append(", 이름: ").append(attraction.getTitle())
                     .append(", 주소: ").append(attraction.getAddr())
                     .append(", 유형: ").append(getContentTypeName(attraction.getContentTypeId()))
                     .append("\n");
           }
       }
       
       // 전체 관광지 목록 (최대 30개)
       prompt.append("\n전체 관광지 목록 (필요시 추가 선택):\n");
       int maxAttractions = Math.min(attractions.size(), 30);
       for (int i = 0; i < maxAttractions; i++) {
           Attraction attraction = attractions.get(i);
           prompt.append(i + 1).append(". ID: ").append(attraction.getNo())
                 .append(", 이름: ").append(attraction.getTitle())
                 .append(", 주소: ").append(attraction.getAddr())
                 .append(", 유형: ").append(getContentTypeName(attraction.getContentTypeId()))
                 .append("\n");
       }
       
       prompt.append("\n중요: 같은 지역 그룹 내의 장소들을 하루 일정에 함께 배치하여 이동 시간을 최소화해주세요.\n");
       prompt.append("각 일차별로 2-3개의 주요 장소를 방문하되, 가까운 장소들끼리 묶어서 구성해주세요.\n\n");
       
       prompt.append("반드시 다음 JSON 형식으로만 응답해주세요. 다른 텍스트는 포함하지 마세요:\n");
       prompt.append("{\n");
       prompt.append("  \"title\": \"여행 제목\",\n");
       prompt.append("  \"description\": \"여행 설명\",\n");
       prompt.append("  \"itinerary\": [\n");
       prompt.append("    {\n");
       prompt.append("      \"day\": 1,\n");
       prompt.append("      \"places\": [\n");
       prompt.append("        {\n");
       prompt.append("          \"id\": 관광지ID숫자,\n");
       prompt.append("          \"name\": \"장소명\",\n");
       prompt.append("          \"description\": \"장소 설명\",\n");
       prompt.append("          \"type\": \"관광지\"\n");
       prompt.append("        }\n");
       prompt.append("      ]\n");
       prompt.append("    }\n");
       prompt.append("  ],\n");
       prompt.append("  \"restaurants\": [\"추천 맛집1\", \"추천 맛집2\"],\n");
       prompt.append("  \"tips\": [\"여행 팁1\", \"여행 팁2\"]\n");
       prompt.append("}\n");
       prompt.append("\n주의사항:\n");
       prompt.append("- 반드시 완전한 JSON 형식으로 응답\n");
       prompt.append("- 모든 배열과 객체를 올바르게 닫기\n");
       prompt.append("- 위에 제공된 관광지 목록에서만 장소 선택\n");
       prompt.append("- 각 장소의 ID를 정확히 포함\n");
       
       log.info("AI 프롬프트 생성 완료");
       
       // AI 서비스 호출
       Object result = chatService.simpleGeneration(prompt.toString());
       log.info("AI 여행 계획 응답 완료");
       
       // 응답 처리 - 더 안전한 방식
       try {
           String resultStr = result.toString().trim();
           log.info("AI 원본 응답 길이: {}", resultStr.length());
           
           // JSON 추출 및 검증
           String cleanJson = extractAndCleanJson(resultStr);
           
           if (cleanJson != null) {
               log.info("추출된 JSON: {}", cleanJson);
               
               try {
                   ObjectMapper mapper = new ObjectMapper();
                   Object parsedJson = mapper.readValue(cleanJson, Object.class);
                   
                   // 관광지 정보 맵 생성
                   Map<Integer, Attraction> attractionMap = attractions.stream()
                       .collect(Collectors.toMap(Attraction::getNo, a -> a));
                   
                   return ResponseEntity.ok(Map.of(
                       "status", "SUCCESS",
                       "plan", parsedJson,
                       "attractions", attractionMap
                   ));
               } catch (JsonProcessingException e) {
                   log.error("JSON 파싱 실패: {}", e.getMessage());
                   log.error("파싱 시도한 JSON: {}", cleanJson);
               }
           }
           
           // JSON 파싱 실패 시 기본 계획 생성
           log.warn("JSON 파싱 실패, 기본 계획 생성");
           Map<String, Object> defaultPlan = createDefaultPlan(clusteredAttractions, tripRequest);
           Map<Integer, Attraction> attractionMap = attractions.stream()
               .collect(Collectors.toMap(Attraction::getNo, a -> a));
           
           return ResponseEntity.ok(Map.of(
               "status", "SUCCESS",
               "plan", defaultPlan,
               "attractions", attractionMap,
               "aiResponse", resultStr,
               "note", "AI 응답 파싱 실패로 기본 계획 제공"
           ));
           
       } catch (Exception e) {
           log.error("AI 응답 처리 중 오류: {}", e.getMessage());
           
           // 완전한 fallback
           Map<String, Object> defaultPlan = createDefaultPlan(clusteredAttractions, tripRequest);
           Map<Integer, Attraction> attractionMap = attractions.stream()
               .collect(Collectors.toMap(Attraction::getNo, a -> a));
           
           return ResponseEntity.ok(Map.of(
               "status", "SUCCESS",
               "plan", defaultPlan,
               "attractions", attractionMap,
               "error", "처리 중 오류 발생"
           ));
       }
   }
   
   /**
    * JSON 추출 및 정리
    */
   private String extractAndCleanJson(String response) {
       if (response == null || response.trim().isEmpty()) {
           return null;
       }
       
       // 마크다운 코드 블록 제거
       response = response.replaceAll("```json\\s*", "").replaceAll("```\\s*", "");
       
       // 첫 번째와 마지막 중괄호 찾기
       int firstBrace = response.indexOf('{');
       int lastBrace = response.lastIndexOf('}');
       
       if (firstBrace == -1 || lastBrace == -1 || firstBrace >= lastBrace) {
           log.warn("유효한 JSON 구조를 찾을 수 없음");
           return null;
       }
       
       String jsonStr = response.substring(firstBrace, lastBrace + 1);
       
       // 기본적인 JSON 정리
       jsonStr = jsonStr.replaceAll("\\s+", " ").trim();
       
       // 간단한 유효성 검사
       if (isBasicValidJson(jsonStr)) {
           return jsonStr;
       }
       
       return null;
   }
   
   /**
    * 기본적인 JSON 유효성 검사
    */
   private boolean isBasicValidJson(String json) {
       if (json == null || json.trim().isEmpty()) {
           return false;
       }
       
       // 기본적인 구조 검사
       int openBraces = 0;
       int closeBraces = 0;
       int openBrackets = 0;
       int closeBrackets = 0;
       
       for (char c : json.toCharArray()) {
           switch (c) {
               case '{': openBraces++; break;
               case '}': closeBraces++; break;
               case '[': openBrackets++; break;
               case ']': closeBrackets++; break;
           }
       }
       
       return openBraces == closeBraces && openBrackets == closeBrackets;
   }
   
   /**
    * 기본 계획 생성 (AI 파싱 실패 시)
    */
   private Map<String, Object> createDefaultPlan(List<List<Attraction>> clusteredAttractions, Map<String, Object> tripRequest) {
       Map<String, Object> plan = new java.util.HashMap<>();
       
       String destination = tripRequest.get("destination").toString();
       int duration = Integer.parseInt(tripRequest.get("duration").toString());
       
       plan.put("title", destination + " " + duration + "박 " + (duration + 1) + "일 여행");
       plan.put("description", "효율적인 동선을 고려한 " + destination + " 여행 계획입니다.");
       
       List<Map<String, Object>> itinerary = new ArrayList<>();
       
       // 각 일차별로 클러스터에서 장소 선택
       for (int day = 0; day < duration; day++) {
           Map<String, Object> dayPlan = new java.util.HashMap<>();
           dayPlan.put("day", day + 1);
           
           List<Map<String, Object>> places = new ArrayList<>();
           
           // 해당 일차에 맞는 클러스터 선택
           if (day < clusteredAttractions.size()) {
               List<Attraction> dayCluster = clusteredAttractions.get(day);
               
               // 클러스터에서 2-3개 장소 선택
               int placesToSelect = Math.min(3, dayCluster.size());
               for (int i = 0; i < placesToSelect; i++) {
                   Attraction attraction = dayCluster.get(i);
                   
                   Map<String, Object> place = new java.util.HashMap<>();
                   place.put("id", attraction.getNo());
                   place.put("name", attraction.getTitle());
                   place.put("description", "효율적인 동선을 고려하여 선정된 " + getContentTypeName(attraction.getContentTypeId()));
                   place.put("type", getContentTypeName(attraction.getContentTypeId()));
                   
                   places.add(place);
               }
           }
           
           dayPlan.put("places", places);
           itinerary.add(dayPlan);
       }
       
       plan.put("itinerary", itinerary);
       plan.put("restaurants", List.of("현지 맛집 추천", "지역 특산물 맛집"));
       plan.put("tips", List.of(
           "같은 지역 내 장소들로 구성하여 이동 시간을 단축했습니다",
           "대중교통 이용 시 환승 정보를 미리 확인하세요",
           "각 장소의 운영시간을 확인하고 방문하세요"
       ));
       
       return plan;
   }
   
   /**
    * 관광지를 거리 기반으로 클러스터링
    */
   private List<List<Attraction>> clusterAttractionsByDistance(List<Attraction> attractions) {
       List<List<Attraction>> clusters = new ArrayList<>();
       List<Attraction> remaining = new ArrayList<>(attractions);
       
       while (!remaining.isEmpty() && clusters.size() < 5) {
           List<Attraction> cluster = new ArrayList<>();
           
           // 첫 번째 장소 선택 (남은 것 중 첫 번째)
           Attraction seed = remaining.remove(0);
           cluster.add(seed);
           
           // 시드 장소에서 가까운 장소들 찾기 (최대 10개)
           for (int i = 0; i < 10 && !remaining.isEmpty(); i++) {
               Attraction nearest = findNearestAttraction(seed, remaining);
               if (nearest != null) {
                   cluster.add(nearest);
                   remaining.remove(nearest);
               }
           }
           
           clusters.add(cluster);
       }
       
       // 남은 장소들은 마지막 클러스터에 추가
       if (!remaining.isEmpty() && !clusters.isEmpty()) {
           clusters.get(clusters.size() - 1).addAll(remaining);
       }
       
       return clusters;
   }
   
   /**
    * 가장 가까운 관광지 찾기
    */
   private Attraction findNearestAttraction(Attraction from, List<Attraction> candidates) {
       if (candidates.isEmpty()) return null;
       
       Attraction nearest = candidates.get(0);
       double minDistance = calculateDistance(
           from.getLatitude(), from.getLongitude(),
           nearest.getLatitude(), nearest.getLongitude()
       );
       
       for (Attraction candidate : candidates) {
           double distance = calculateDistance(
               from.getLatitude(), from.getLongitude(),
               candidate.getLatitude(), candidate.getLongitude()
           );
           
           if (distance < minDistance) {
               minDistance = distance;
               nearest = candidate;
           }
       }
       
       return nearest;
   }
   
   /**
    * 두 지점 간의 거리 계산 (하버사인 공식)
    */
   private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
       final int R = 6371; // 지구 반지름 (km)
       
       double latDistance = Math.toRadians(lat2 - lat1);
       double lonDistance = Math.toRadians(lon2 - lon1);
       
       double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
               + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
               * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
       
       double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
       
       return R * c; // 거리 (km)
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
