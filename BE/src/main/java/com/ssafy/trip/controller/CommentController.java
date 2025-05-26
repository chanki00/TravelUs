package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.dto.Comment;
import com.DB_PASSWORD_REDACTED.trip.dto.comment.InsertCommentDTO;
import com.DB_PASSWORD_REDACTED.trip.dto.comment.UpdateCommentDTO;
import com.DB_PASSWORD_REDACTED.trip.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
	
	private final CommentService service;

	@GetMapping("/plan/{planId}")
	public ResponseEntity<?> getCommentbyPlanId(@PathVariable int planId){
		List<Comment> comments = service.getCommentbyPlanId(planId);
		return ResponseEntity.ok(comments);
	}
	
	@PostMapping("")
	public ResponseEntity<?> createComment(@RequestBody InsertCommentDTO req){
		int res = service.createComment(req);
		return ResponseEntity.ok(res);
	}
	
	@PutMapping("/{commentId}")
	public ResponseEntity<Comment> updateComment(@PathVariable int commentId, @RequestBody UpdateCommentDTO req){
		Comment res = service.updateComment(commentId, req);
		return ResponseEntity.ok(res);
	}
	
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable int commentId){
		service.deleteComment(commentId);
		return ResponseEntity.ok().build();
	}
}
