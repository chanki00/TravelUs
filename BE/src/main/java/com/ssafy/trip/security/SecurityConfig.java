package com.DB_PASSWORD_REDACTED.trip.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.DB_PASSWORD_REDACTED.trip.repository.RefreshRepository;
import com.DB_PASSWORD_REDACTED.trip.security.jwt.JWTAuthenticationFilter;
import com.DB_PASSWORD_REDACTED.trip.security.jwt.JWTUtil;
import com.DB_PASSWORD_REDACTED.trip.security.jwt.JWTVerificationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.withDefaultRolePrefix().role("ADMIN").implies("USER").build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    JWTAuthenticationFilter jwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            RefreshRepository refreshRepository) {

        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager, jwtUtil, refreshRepository);
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/api/v1/user/login"); // 로그인 URL
        return filter;
    }
    
	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://INTERNAL_IP_REDACTED:5173")); // ★ 프론트 주소로
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // ★ 꼭 추가

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // ★ 전역 설정

        return source;
    }

	@Bean
    SecurityFilterChain apiSecurityFilterChain(
            HttpSecurity http,
            @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfig,
            CustomUserDetailsService userDetailsService,
            JWTAuthenticationFilter authFilter,
            JWTVerificationFilter jwtVerifyFilter)
            throws Exception {
		
    	http.securityMatcher("/api/**")
    	.cors(t -> t.configurationSource(corsConfig))
    	.userDetailsService(userDetailsService)
    	.csrf(csrf -> csrf.disable())
    	.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    	http.authorizeHttpRequests(auth -> auth
    		    .requestMatchers("/api/v1/user", "/api/v1/user/**").permitAll()  // 회원가입 등 허용
    		    .requestMatchers("/api/auth/**").permitAll() 
    		    .requestMatchers("/api/v1/chat").authenticated()// 로그인/재발급 허용
//    		    .anyRequest().authenticated()                                    // 나머지는 인증 필요
    		 //  TODO : 테스트용( 제거 해야됨)
    		    .anyRequest().permitAll() 
    		);

    	
    	http.addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)
    	.addFilterAt(authFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}
}
