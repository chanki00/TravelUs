package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.io.IOException;import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
		
		String token = resolveToken(request);
		
		if (token == null || jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		filterChain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        
        if (token != null && token.startsWith("Bearer ")) {
        	return token.substring(7);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
	

	
	
}
