package com.DB_PASSWORD_REDACTED.trip.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.Comment;
import com.DB_PASSWORD_REDACTED.trip.dto.comment.InsertCommentDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.comment.UpdateCommentDTO;
import com.DB_PASSWORD_REDACTED.trip.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository repo;

	public List<Comment> getCommentbyPlanId(int planId) {
		return repo.getCommentbyPlanId(planId);
	}

	public int createComment(InsertCommentDTO req) {
		return repo.createComment(req);
	}

	public int updateComment(int commentId, String string) {
		return repo.updateComment(commentId, string);
	}

	public int deleteComment(int commentId) {
		return repo.deleteComment(commentId);
	}
}
