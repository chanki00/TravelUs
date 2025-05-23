package com.DB_PASSWORD_REDACTED.trip.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
	
	@Value("${DB_PASSWORD_REDACTED.ai.system-prompt}")
	String systemPrompt;
	
	ChatClient simpleChatClient(ChatClient.Builder builder) {
		return builder.defaultSystem(systemPrompt).build();
	}
}
