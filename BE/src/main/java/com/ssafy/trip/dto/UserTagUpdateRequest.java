package com.DB_PASSWORD_REDACTED.trip.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserTagUpdateRequest {
    private int userId;
    private List<Integer> tagIds;
}
