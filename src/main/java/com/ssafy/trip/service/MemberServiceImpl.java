package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;
import com.DB_PASSWORD_REDACTED.trip.repository.MemberRepository;
import com.DB_PASSWORD_REDACTED.trip.util.HashUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository repo;
	private final PasswordEncoder pe;

	@Override
	public int insertMember(MemberDto member) throws Exception {
		
//		String hash = HashUtil.hashToString(member.getId(), member.getPw());
		member.setPw(pe.encode(member.getPw()));
		
		int cnt = repo.insertMember(member);
		return cnt;
	}

	@Override
	public MemberDto selectMember(String id) throws Exception {
		return repo.selectMember(id);
	}

	@Override
	public List<MemberDto> selectAllMember() throws Exception {
		return repo.selectAllMember();
	}

	@Override
	public int updateInfo(String id, String name, String email) throws Exception {
		int cnt = repo.updateInfo(id, name, email);
		return cnt;
	}
	
	@Override
	public int updatePw(String id, String pw) throws Exception {
				
//		String hash = HashUtil.hashToString(id, pw);
		
		int cnt = repo.updatePw(id, pe.encode(pw));
		return cnt;
	}

	@Override
	public int deleteMember(String id) throws Exception {
		int cnt = repo.deleteMember(id);
		return cnt;
	}

	@Override
	public boolean login(String id, String pw) throws Exception {
		MemberDto dto = this.selectMember(id);
		
//		String hash = HashUtil.hashToString(id, pw);
		
		if (dto != null && pe.matches(pw, dto.getPw())) {
			return true;
		}
		
		return false;
	}
}
