package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserInfoDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserPwDto;

public interface UserService {
	public int insertUser(UserDto user);
	public UserDto selectUser(Long id);
	public List<UserDto> selectAllUser();
	public int updateInfo(UserInfoDto dto);
	public int updatePw(UserPwDto dto);
	public int deleteUser(Long id);
	boolean login(Long id, String userId, String userPw);
}
