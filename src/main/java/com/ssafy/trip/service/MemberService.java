package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;

public interface MemberService {
	public int insertMember(MemberDto member) throws Exception;
	public MemberDto selectMember(String id) throws Exception;
	public List<MemberDto> selectAllMember() throws Exception;
	public int updateInfo(String id, String name, String email) throws Exception;
	public int updatePw(String id, String pw) throws Exception;
	public int deleteMember(String id) throws Exception;
	boolean login(String id, String pw) throws Exception;
}
