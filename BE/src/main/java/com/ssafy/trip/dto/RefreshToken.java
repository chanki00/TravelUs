package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private String username;
    private String token;
}