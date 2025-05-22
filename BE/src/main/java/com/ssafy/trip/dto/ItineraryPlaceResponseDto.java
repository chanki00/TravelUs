package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class ItineraryPlaceResponseDto {
	// Itinerary 필드
    private int dayId;
    private int dayNumber;       // TripPlanDays.day_number 추가
    private int attractionId;
    private int placeOrder;
    private String memo;

    // Attraction 필드
    private int no;
    private int contentId;
    private String title;
    private int contentTypeId;
    private int areaCode;
    private int sigunguCode;
    private String image;
    private int mapLevel;
    private double latitude;
    private double longitude;
    private String tel;
    private String addr;
    private String homepage;
    private String overview;
}
