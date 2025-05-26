package com.DB_PASSWORD_REDACTED.trip.oauth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.DB_PASSWORD_REDACTED.trip.security.jwt.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final JWTUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
		
		String token = jwtUtil.createJwt("accessToken", oauthUser.getUsername(), oauthUser.getRole(), 60*60*1000L);
		
		Cookie cookie = new Cookie("Authorization", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(60*60);
		
		response.addCookie(cookie);
		response.sendRedirect("http://localhost:5173/");
	}
	
	
}
