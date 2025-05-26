package com.DB_PASSWORD_REDACTED.trip.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User, UserDetails {
	
	private final UserDto userDto;
	private final Map<String, Object> attributes;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		return userDto.getUserId();
	}
	
	@Override
	public String getPassword() {
		return null;
	}
	
	@Override
	public String getUsername() {
		return userDto.getUserId();
	}
	
	public String getRole() {
		return userDto.getRole().name();
	}
	
	
}
