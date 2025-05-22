package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.TagDTO;
import com.DB_PASSWORD_REDACTED.trip.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
	
	private final TagRepository repo;
	
	@Override
	public List<TagDTO> getTripTags() {
		return repo.getTripTags();
	}

	public int insertplanTag(int planId, int tagId) {
		return repo.insertplanTag(planId,tagId);
	}

	public List<String> getTripplanTagsName(int planId) {
		return repo.getTripplanTagsName(planId);
	}
	
	public List<TagDTO> getUserTags() {
		return repo.getUserTags();
	}
	
	public int insertUserTag(int userId, int tagId) {
		return repo.insertUserTag(userId, tagId);
	}
	
	public List<String> getUserTagsName(int userId) {
		return repo.getUserTagsName(userId);
	}
	
	public void updateUserTags(int userId, List<Integer> tagIds) {
	    repo.deleteUserTags(userId); // 기존 유저 태그 삭제
	    for (Integer tagId : tagIds) {
	        repo.insertUserTag(userId, tagId); // 새 태그 등록
	    }
	}

}
