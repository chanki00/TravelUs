package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.DB_PASSWORD_REDACTED.trip.dto.trip.TripPost;

@Mapper
public interface PostRepository {
	public List<TripPost> getPagedPosts(int offset, int limit);
	public int getTotalPostCount();
	public List<TripPost> getAllPosts();
}
