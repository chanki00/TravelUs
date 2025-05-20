package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.Attraction;
import com.DB_PASSWORD_REDACTED.trip.dto.Sido;

@Mapper
public interface TripRepository {

	List<Sido> getSidoList();

		List<Attraction> getAttractionBysidoCode(int sidoCode);

}
