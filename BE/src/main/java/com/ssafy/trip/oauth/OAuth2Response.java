package com.DB_PASSWORD_REDACTED.trip.oauth;

public interface OAuth2Response {
    String getProvider();     // naver, google 등
    String getProviderId();   // 소셜 제공자의 유저 고유 ID
    String getEmail();
    String getName();
}
