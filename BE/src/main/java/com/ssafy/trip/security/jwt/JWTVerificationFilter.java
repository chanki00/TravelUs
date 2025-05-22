package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JWTVerificationFilter extends OncePerRequestFilter {
	
	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String accessToken = extractToken(request);
		
		if (accessToken == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if (jwtUtil.isExpired(accessToken)) {
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    return;
		}

		Claims claims = jwtUtil.getClaims(accessToken);
		UserDetails details = userDetailsService.loadUserByUsername(claims.get("username").toString());
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	
		filterChain.doFilter(request, response);
	}
	
	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader("accessToken");
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		
		return null;
	}
	
	
	
}
