package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.trip.Guguns;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.Sidos;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripRegionDto;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentData;
import com.DB_PASSWORD_REDACTED.trip.dto.trip.contentTypes;

@Mapper
public interface TripRepository {
    List<Sidos> getAllSidos();

    Sidos getSidoByCode(int sidoCode);
    
    List<Guguns> getGugunsBySidoCode(int sidoCode);

    List<contentTypes> getContentTypes();
    
    List<contentData> contentSearch(String sido, String gugun, String content);
}
