package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserInfoDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserPwDto;
import com.DB_PASSWORD_REDACTED.trip.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repo;

	@Override
	public int insertUser(UserDto user) {
		
		int cnt = repo.insertUser(user);
		
		return cnt;
	}

	@Override
	public UserDto selectUser(Long id) {		
		return repo.selectUser(id);
	}

	@Override
	public List<UserDto> selectAllUser() {
		return repo.selectAllUser();
	}

	@Override
	public int updateInfo(UserInfoDto dto) {
		
		int cnt = repo.updateInfo(dto);
		
		return cnt;
	}

	@Override
	public int updatePw(UserPwDto dto) {
		
		int cnt = updatePw(dto);
		
		return cnt;
	}

	@Override
	public int deleteUser(Long id) {
		
		int cnt = repo.deleteUser(id);
		
		return cnt;
	}

	@Override
	public boolean login(Long id, String userId, String userPw) {
		return false;
	}
	
}
