package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.Attraction;
import com.DB_PASSWORD_REDACTED.trip.dto.Sido;
import com.DB_PASSWORD_REDACTED.trip.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService{
	
	private final TripRepository repo;
	
	@Override
	public List<Sido> getSidoList() {
		return repo.getSidoList();
	}

	public List<Attraction> getAttractionBysidoCode(int sidoCode) {
		return repo.getAttractionBysidoCode(sidoCode);
	}
	
}
