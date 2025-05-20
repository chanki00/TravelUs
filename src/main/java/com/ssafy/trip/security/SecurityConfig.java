package com.DB_PASSWORD_REDACTED.trip.security;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.http.Cookie;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.withDefaultRolePrefix()
				.role("admin").implies("user")
				.role("user").implies("guest")
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
         .csrf(csrf -> csrf.disable())
         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
         .sessionManagement(session -> session
             .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
         .authorizeHttpRequests(authorize -> authorize
             .requestMatchers("/member/secured/admin/**").hasRole("admin")
             .requestMatchers("/member/secured/**").hasRole("user")
             .requestMatchers("/auth/**").authenticated()
             .anyRequest().permitAll()
         )
         .formLogin(form -> form
             .loginProcessingUrl("/member/login")
             .usernameParameter("id")
             .passwordParameter("pw")
             .successHandler((request, response, authentication) -> {
                 // 로그인 성공 시 JSON 응답
                 response.setStatus(200);
                 response.setContentType("application/json;charset=UTF-8");

                 // 쿠키 처리 (remember me)
                 String remember = request.getParameter("remember");
                 Cookie cookie = new Cookie("idsave", authentication.getName());
                 cookie.setPath("/");
                 cookie.setMaxAge(remember != null ? 60 * 60 * 10 : 0);
                 response.addCookie(cookie);

                 PrintWriter writer = response.getWriter();
                 writer.write("{\"message\": \"로그인 성공\"}");
                 writer.flush();
             })
             .failureHandler((request, response, exception) -> {
                 response.setStatus(401);
                 response.setContentType("application/json;charset=UTF-8");

                 PrintWriter writer = response.getWriter();
                 writer.write("{\"error\": \"아이디 또는 비밀번호가 틀렸습니다.\"}");
                 writer.flush();
             })
             .permitAll()
         )
         .logout(logout -> logout
             .logoutUrl("/member/logout")
             .invalidateHttpSession(true)
             .deleteCookies("JSESSIONID", "idsave")
             .logoutSuccessHandler((request, response, authentication) -> {
                 response.setStatus(200);
                 response.setContentType("application/json;charset=UTF-8");
                 response.getWriter().write("{\"message\": \"로그아웃 성공\"}");
             })
         );

     return http.build();
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키 허용
        config.setAllowedOrigins(List.of("http://localhost:5173")); // Vue 개발 서버 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
	@Bean
	public CorsFilter corsFilter() {
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("http://localhost:5173");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);
	    return new CorsFilter(source);
	}
}
