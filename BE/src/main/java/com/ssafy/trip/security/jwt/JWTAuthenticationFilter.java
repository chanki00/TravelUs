package com.DB_PASSWORD_REDACTED.trip.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.DB_PASSWORD_REDACTED.trip.dto.RefreshToken;
import com.DB_PASSWORD_REDACTED.trip.dto.user.LoginDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserResponseDto;
import com.DB_PASSWORD_REDACTED.trip.repository.RefreshRepository;
import com.DB_PASSWORD_REDACTED.trip.security.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final RefreshRepository repo;

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

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		UserDto userDto = userDetails.getUser();
		UserResponseDto user = new UserResponseDto(userDto.getId(), userDto.getUserId(), 
				userDto.getUserEmail(), userDto.getName(), userDto.getAge(), userDto.getGender(), 
				userDto.getAddress(), userDto.getIntro(), userDto.getRole(), userDto.getAllowInvite());
				
				
		String accessToken = jwtUtil.createJwt("accessToken", user.getUserId(), user.getRole().name(), 60 * 60 * 1000L);
		String refreshToken = jwtUtil.createJwt("refreshToken", user.getUserId(), user.getRole().name(),
				24 * 60 * 60 * 1000L);

		repo.saveToken(new RefreshToken(user.getUserId(), refreshToken));

		Map<String, Object> tokenMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken, "user", user);
		String json = new ObjectMapper().writeValueAsString(tokenMap);
		response.setContentType("application/json");
		response.getWriter().write(json);

	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		log.warn("로그인 실패: {}", failed.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		Map<String, String> error = Map.of("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
		String json = new ObjectMapper().writeValueAsString(error);

		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	}

}
