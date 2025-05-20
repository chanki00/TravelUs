package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.Attraction;
import com.DB_PASSWORD_REDACTED.trip.dto.Sido;
import com.DB_PASSWORD_REDACTED.trip.dto.Tripplan;
import com.DB_PASSWORD_REDACTED.trip.service.TripServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RestTripController {
	
	private final TripServiceImpl service;
	
	@GetMapping("/sidos")
    public ResponseEntity<List<Sido>> getSidos() {
        return ResponseEntity.ok(service.getSidoList());
    }
	
	@GetMapping("/attractions/{sidoCode}")
	public ResponseEntity<List<Attraction>> getAttractionBysidoCode(@PathVariable int sidoCode){
		return ResponseEntity.ok(service.getAttractionBysidoCode(sidoCode));
	}
}
