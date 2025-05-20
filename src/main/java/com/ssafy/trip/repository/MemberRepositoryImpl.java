/*
package com.DB_PASSWORD_REDACTED.trip.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.DB_PASSWORD_REDACTED.trip.dto.MemberDto;
import com.DB_PASSWORD_REDACTED.trip.util.DBUtil;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

	@Override
	public int insertMember(Connection conn, MemberDto member) throws Exception {
		
		String sql = "INSERT INTO member (id, pw, name, email) VALUES (?, ?, ?, ?)";
		int cnt = 0;
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPw());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			
			cnt = pstmt.executeUpdate();
		}
		
		return cnt;
	}

	@Override
	public MemberDto selectMember(String id) throws Exception {
		
		String sql = "SELECT * FROM member WHERE id = ?";
		MemberDto member = null;
		
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				member = new MemberDto();
				member.setId(rs.getString("id"));
				member.setPw(rs.getString("pw"));
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
			}
		}
		
		return member;
	}

	@Override
	public List<MemberDto> selectAllMember() throws Exception {
		
		String sql = "SELECT * FROM member";
		List<MemberDto> members = new ArrayList<>();
		
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberDto member = new MemberDto();
				member.setId(rs.getString("id"));
				member.setPw(rs.getString("pw"));
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				members.add(member);
			}
		}
		
		return members;
	}

	@Override
	public int updateInfo(Connection conn, String id, String name, String email) throws Exception {
		
//		String sql = "UPDATE member SET pw = ?, name = ?, email = ? WHERE id = ?";
		String sql = "UPDATE member SET name = ?, email = ? WHERE id = ?";
		int cnt = 0;
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//			pstmt.setString(1, member.getPw());
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			pstmt.setString(3, id);
			
			cnt = pstmt.executeUpdate();
		}
		
		return cnt;
	}
	
	@Override
	public int updatePw(Connection conn, String id, String pw) throws Exception {
		
		String sql = "UPDATE member SET pw = ? WHERE id = ?";
//		String sql = "UPDATE member SET name = ?, email = ? WHERE id = ?";
		int cnt = 0;
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, pw);
			pstmt.setString(2, id);
			
			cnt = pstmt.executeUpdate();
		}
		
		return cnt;
	}

	@Override
	public int deleteMember(Connection conn, String id) throws Exception {
		
		String sql = "DELETE FROM member WHERE id = ?";
		int cnt = 0;
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			
			cnt = pstmt.executeUpdate();
		}
		
		return cnt;
	}

	

}
*/