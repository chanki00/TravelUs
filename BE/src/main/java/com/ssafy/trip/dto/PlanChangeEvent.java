package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanChangeEvent {
    private String type; // TITLE_CHANGE, DESCRIPTION_CHANGE, ITINERARY_CHANGE, USER_JOIN, USER_LEAVE
    private int planId;
    private int userId;
    private String userName;
    private Object data;
    private long timestamp;
    private int version;
}
