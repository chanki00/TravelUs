package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Itinerary {
	private int dayId;
	private int attractionId;
	private int placeOrder;
	private String memo;
	
}
