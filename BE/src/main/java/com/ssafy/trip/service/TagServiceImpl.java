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

}
