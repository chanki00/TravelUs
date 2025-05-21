package com.DB_PASSWORD_REDACTED.trip.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
public class SecurityConfig {

	@Bean
	public RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.withDefaultRolePrefix().role("ADMIN").implies("USER").build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// cors 설정
		http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.addAllowedOriginPattern("*"); // 모든 도메인 허용 (개발용)
				config.addAllowedHeader("*");
				config.addAllowedMethod("*");
				config.setAllowCredentials(true); // 자격 증명 포함 허용
				config.addExposedHeader("Authorization");

				return config;
			}
		}));

		http.csrf(auth -> auth.disable());

		http.formLogin(member -> member.loginPage("/member/loginform").loginProcessingUrl("/member/login")
				.usernameParameter("id").passwordParameter("pw").failureUrl("/member/loginform?")
				.successHandler((request, response, authentication) -> {
					String remember = request.getParameter("remember");
					Cookie cookie = new Cookie("idsave", authentication.getName());
					cookie.setPath("/");
					if (remember != null) {
						cookie.setMaxAge(60 * 60 * 10);
					} else {
						cookie.setMaxAge(0);
					}

					response.addCookie(cookie);
					response.sendRedirect(request.getContextPath() + "/");
				}).permitAll());

		http.logout(member -> member.logoutUrl("/member/logout").invalidateHttpSession(true).logoutSuccessUrl("/"));

		return http.build();

	}
}
