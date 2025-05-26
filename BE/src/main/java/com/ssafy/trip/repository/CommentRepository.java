package com.DB_PASSWORD_REDACTED.trip.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.DB_PASSWORD_REDACTED.trip.dto.Comment;
import com.DB_PASSWORD_REDACTED.trip.dto.comment.InsertCommentDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.comment.UpdateCommentDTO;

@Mapper
public interface CommentRepository {

	List<Comment> getCommentbyPlanId(int planId);

	int createComment(InsertCommentDTO req);

	int updateComment(int commentId, String content);

	int deleteComment(int commentId);

}
