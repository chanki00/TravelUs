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
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class JwtController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository repo;

    /**
     * RefreshToken을 이용하여 새로운 AccessToken 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> reissue(@RequestHeader("refreshToken") String refreshToken) {

        if (jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 만료");
        }

        Claims claims = jwtUtil.getClaims(refreshToken);
        String username = claims.get("username", String.class);

        RefreshToken savedToken = repo.findByUsername(username);
        if (savedToken == null || !savedToken.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 불일치");
        }

        String role = claims.get("role", String.class);
        String newAccessToken = jwtUtil.createJwt("accessToken", username, role, 600000L);


        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, @RequestHeader(value="refreshtoken", required = false) String refreshToken) {
    	if (!(refreshToken == null || refreshToken.isEmpty())) {
    		Claims claims = jwtUtil.getClaims(refreshToken);
    		String username = (String) claims.get("username");
    		if (username == null) {
    			throw new JwtException("username이 없습니다.");
    		}
    		
    		repo.deleteToken(username);
    	}
    	
    	
        // ✅ accessToken 쿠키 제거
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setHttpOnly(true); // 기존과 동일하게 설정
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 만료
        cookie.setSecure(false);
        
        System.out.println("쿠키 제거됨");
        
        response.addCookie(cookie);
    	
    	return ResponseEntity.ok("로그아웃 완료");
    }
}