package com.DB_PASSWORD_REDACTED.trip.service;

import java.sql.SQLException;
import java.util.List;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserInfoDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserPwDto;

public interface UserService {
	public int insertUser(UserDto user) throws SQLException;
	public UserDto selectUser(int id) throws SQLException;
	public List<UserDto> selectAllUser() throws SQLException;
	public int updateInfo(UserInfoDto dto) throws SQLException;
	public int updatePw(UserPwDto dto) throws SQLException;
	public int deleteUser(int id) throws SQLException;
	public UserDto getUserById(int userId);
}
