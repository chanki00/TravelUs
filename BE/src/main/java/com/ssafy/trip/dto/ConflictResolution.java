package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConflictResolution {
    private String conflictType;
    private Object currentValue;
    private Object incomingValue;
    private int currentVersion;
    private int incomingVersion;
    private String lastModifiedBy;
    private long lastModifiedAt;
}
