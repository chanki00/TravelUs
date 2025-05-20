package com.DB_PASSWORD_REDACTED.trip.repository;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;

@Mapper
public interface MemberRepository {
	public int insertMember(MemberDto member) throws SQLException;
	public MemberDto selectMember(String id) throws SQLException;
	public List<MemberDto> selectAllMember() throws SQLException;
	public int updateInfo(String id, String name, String email) throws SQLException;
	public int updatePw(String id, String pw) throws SQLException;
	public int deleteMember(String id) throws SQLException;
}
