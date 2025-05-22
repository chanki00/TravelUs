package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.TagDTO;

@Mapper
public interface TagRepository {

	List<TagDTO> getTripTags();
	int insertplanTag(int planId, int tagId);
	List<String> getTripplanTagsName(int planId);
	
	List<TagDTO> getUserTags();
	int insertUserTag(int userId, int tagId);
	List<String> getUserTagsName(int userId);
	
    int deleteUserTags(int userId);
}
