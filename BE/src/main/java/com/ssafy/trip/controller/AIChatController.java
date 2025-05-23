package com.DB_PASSWORD_REDACTED.trip.controller;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DB_PASSWORD_REDACTED.trip.service.AiChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AIChatController {
	private final AiChatService chatService;
	
	@PostMapping("/simple")
    ResponseEntity<?> simpleGeneration(@RequestBody Map<String, String> userInput) {
        Object result = chatService.simpleGeneration(userInput.get("message"));
        
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", result
            ));
    }
}
