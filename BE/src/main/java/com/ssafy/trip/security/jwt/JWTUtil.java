package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {
	private SecretKey secretKey;

	public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
				Jwts.SIG.HS256.key().build().getAlgorithm());
	}
	
	@Value("${DB_PASSWORD_REDACTED.jwt.access-expmin}")
    private long accessExpMin;

    @Value("${DB_PASSWORD_REDACTED.jwt.refresh-expmin}")
    private long refreshExpMin;
    
    public String createAccessToken(Map<String, Object> claims) {
        return createToken("accessToken", accessExpMin, claims);
    }
    
    public String createRefreshToken(Map<String, Object> claims) {
        return createToken("refreshToken", refreshExpMin, claims);
    }
    
    private String createToken(String subject, long expireMinutes, Map<String, Object> claims) {

        return Jwts.builder()
                .subject(subject) // "accessToken" or "refreshToken"
                .claims(claims)   // 사용자 정보 (email, role, name 등)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireMinutes))
                .signWith(secretKey) // HMAC-SHA256 서명
                .compact();       // JWT 문자열 생성
    }
    
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)   // 서명 검증
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // 7. 이 토큰이 access인지 refresh인지 확인
    public String getTokenSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
