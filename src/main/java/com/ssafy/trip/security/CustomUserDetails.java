package com.DB_PASSWORD_REDACTED.trip.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {
	private @NonNull MemberDto member;
	private final @NonNull PasswordEncoder pe;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> roles = new ArrayList<>();
		
		if (member.getRole() != null) {
			roles.add(new SimpleGrantedAuthority("ROLE_"+member.getRole()));
		}
		
		return roles;
	}
	@Override
	public String getPassword() {
		return member.getPw();
	}
	@Override
	public String getUsername() {
		return member.getId();
	}
	
	
}
