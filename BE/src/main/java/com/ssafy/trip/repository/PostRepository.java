package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.Post;

@Mapper
public interface PostRepository {

	int createRecruitPost(Post req);

	List<Post> getALlRecruitPost();

}
