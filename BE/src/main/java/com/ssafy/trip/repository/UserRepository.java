package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserInfoDto;
import com.DB_PASSWORD_REDACTED.trip.dto.user.UserPwDto;

@Mapper
public interface UserRepository {
	public int insertUser(UserDto user);
	public UserDto selectUser(Long id);
	public List<UserDto> selectAllUser();
	public int updateInfo(UserInfoDto dto);
	public int updatePw(UserPwDto dto);
	public int deleteUser(Long id);
}
