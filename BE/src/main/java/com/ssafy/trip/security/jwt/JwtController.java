package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.RefreshToken;
import com.DB_PASSWORD_REDACTED.trip.repository.RefreshRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class JwtController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshTokenMapper;

    /**
     * RefreshToken을 이용하여 새로운 AccessToken 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("refreshToken") String refreshToken) {

        // 1. 만료 확인
        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken이 만료되었습니다.");
        }

        // 2. 토큰 정보에서 username 추출
        Claims claims = jwtUtil.getClaims(refreshToken);
        String username = claims.get("username", String.class);

        // 3. DB의 refresh token과 비교
        RefreshToken savedToken = refreshTokenMapper.findByUsername(username);
        if (savedToken == null || !savedToken.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken이 일치하지 않습니다.");
        }

        // 4. 새 access token 발급
        String role = claims.get("role", String.class); // 없으면 기본값으로도 가능
        String newAccessToken = jwtUtil.createAccessToken(Map.of("username", username, "role", role));

        // 5. 응답
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}