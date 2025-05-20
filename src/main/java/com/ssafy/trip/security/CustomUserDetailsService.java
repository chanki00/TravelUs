package com.DB_PASSWORD_REDACTED.trip.security;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;
import com.DB_PASSWORD_REDACTED.trip.repository.MemberRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements  UserDetailsService {

	private final MemberRepository repo;
	private final @NonNull PasswordEncoder pe;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberDto member = null;
		
		try {
			member = repo.selectMember(username);
			if (member == null) {
				throw new UsernameNotFoundException(username);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return new CustomUserDetails(member, pe);
	}
	
	
	
}
