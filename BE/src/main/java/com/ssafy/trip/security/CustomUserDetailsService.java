package com.DB_PASSWORD_REDACTED.trip.security;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository repo;
	private final @NonNull PasswordEncoder pe;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDto user = null;
		
		try {
			user = repo.selectUserByUserid(username);
			
			System.out.println("로그인 유저: " + user);
			
			if (user == null) {
				throw new UsernameNotFoundException(username);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new CustomUserDetails(user, pe);
	}
	
	
}
