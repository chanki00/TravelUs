package com.DB_PASSWORD_REDACTED.trip.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {

	@Qualifier("simpleChatClient")
	private final ChatClient simpleChatClient;
	
	public Object simpleGeneration(String userInput) {
		var spec = simpleChatClient.prompt()
									.system(t -> t.param("language", "korean")
											.param("character", "chill)"))
									.user(userInput)
									.call();
		return spec.content();
	}
}
