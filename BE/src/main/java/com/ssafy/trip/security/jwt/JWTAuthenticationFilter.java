package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.DB_PASSWORD_REDACTED.trip.dto.user.LoginDto;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		UsernamePasswordAuthenticationToken authToken = null;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			LoginDto login = mapper.readValue(request.getInputStream(), LoginDto.class);
			
			authToken = new UsernamePasswordAuthenticationToken(login.getUserId(), login.getUserPw());
			
			return authenticationManager.authenticate(authToken);
			
			
		} catch (IOException e) {
			throw new RuntimeException("로그인 정보 파싱 실패");
		}
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("로그인 성공");
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		String username = customUserDetails.getUsername();
		Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
		
		String role = new String();
		for (GrantedAuthority grantedAuthority : authorities) {
			role = grantedAuthority.getAuthority();
		}
		
		// AccessToken, RefreshToken
		
		
		
		
		
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
	}
	
	
	
}
