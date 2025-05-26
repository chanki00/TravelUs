package com.DB_PASSWORD_REDACTED.trip.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder pe;

	@Override
	public int insertUser(UserDto user) throws SQLException {
		
		user.setUserPw(pe.encode(user.getUserPw()));
		
		int cnt = repo.insertUser(user);
		
		return cnt;
	}

	@Override
	public UserDto selectUser(int id) throws SQLException {		
		return repo.selectUser(id);
	}

	@Override
	public List<UserDto> selectAllUser() throws SQLException {
		return repo.selectAllUser();
	}

	@Override
	public int updateInfo(UserInfoDto dto) throws SQLException {
		
		int cnt = repo.updateInfo(dto);
		
		return cnt;
	}

	@Override
	public int updatePw(UserPwDto dto) throws SQLException {
		
		dto.setPw(pe.encode(dto.getPw()));
		
		int cnt = repo.updatePw(dto);
		
		return cnt;
	}

	@Override
	public int deleteUser(int id) throws SQLException {
		
		int cnt = repo.deleteUser(id);
		
		return cnt;
	}

	 @Override
	    public UserDto getUserById(int userId) {
	        return repo.getUserById(userId);
	    }
	
}
