package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.Post;
import com.DB_PASSWORD_REDACTED.trip.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
	private final PostRepository repo;

	public int createRecruitPost(Post req) {
		return repo.createRecruitPost(req);
	}

	public List<Post> getALlRecruitPost() {
		return repo.getALlRecruitPost();
	}
}
