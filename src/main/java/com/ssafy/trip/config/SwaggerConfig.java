package com.DB_PASSWORD_REDACTED.trip.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


// http://localhost:8080/swagger-ui.html
@Configuration
@OpenAPIDefinition(info = @Info(title = "SSAFY TRIP API 명세서"))
public class SwaggerConfig {
	@Bean
	GroupedOpenApi memberOpenApi() {
		String[] paths = {"/api/v1/member/**"};
		return GroupedOpenApi.builder().group("Member 관련 API").pathsToMatch(paths).build();
	}
	
	@Bean
	GroupedOpenApi tripOpenApi() {
		String[] paths = {"/api/v1/trip/**"};
		return GroupedOpenApi.builder().group("Trip 관련 API").pathsToMatch(paths).build();
	}
}
