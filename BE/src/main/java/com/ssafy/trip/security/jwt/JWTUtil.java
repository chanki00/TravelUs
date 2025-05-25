package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${DB_PASSWORD_REDACTED.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
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
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expireMinutes * 60 * 1000;

        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(expMillis))
                .claims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰. claims 추출만 진행합니다.");
            return e.getClaims(); // payload는 추출 가능
        } catch (JwtException e) {
            log.error("JWT 파싱 오류", e);
            throw e; // 다른 오류는 그대로 던짐
        }
    }

    public boolean isExpired(String token) {
        try {
            Date exp = getClaims(token).getExpiration();
            return exp.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String getTokenSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject(); // 만료된 경우에도 subject는 추출 가능
        }
    }
}
