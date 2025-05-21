package com.DB_PASSWORD_REDACTED.trip.repository;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.RefreshToken;

@Mapper
public interface RefreshRepository {
    void saveToken(RefreshToken token); // INSERT OR UPDATE
    RefreshToken findByUsername(String username);
    void deleteToken(String username);
}
