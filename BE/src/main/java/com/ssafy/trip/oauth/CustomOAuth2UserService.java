package com.DB_PASSWORD_REDACTED.trip.oauth;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.DB_PASSWORD_REDACTED.trip.dto.user.UserDto;
import com.DB_PASSWORD_REDACTED.trip.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository repo;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("naver")) {
			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else {
			throw new OAuth2AuthenticationException("Only Naver is supported currently.");
		}

		String provider = userRequest.getClientRegistration().getRegistrationId(); // "naver"
		Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttribute("response");
		String providerId = (String) response.get("id");
		String name = (String) response.get("name");
		String email = (String) response.get("email");

		String username = provider + " " + providerId;
		System.out.println("유저네임: " + username);

		UserDto user = null;
		try {
			user = repo.selectUserByUserid(username);

			System.out.println("유저아이디: " + user.getUserId());

			if (user == null) {
				// 신규 회원 등록
				user = new UserDto();
				user.setUserId(username);
				user.setUserPw("SOCIAL"); // 소셜 로그인용 더미 비밀번호
				user.setUserEmail(oAuth2Response.getEmail());
				user.setName(oAuth2Response.getName());
//            user.setAge("0");
//            user.setGender(Gender.);
//            user.setAddress("소셜 가입자");
//            user.setRole(Role.ROLE_USER);
				user.setAllowInvite(true);
//				user.setCreatedAt(LocalDateTime.now());
				repo.insertUser(user);
				return new CustomOAuth2User(user, oAuth2User.getAttributes());
			}

			return new CustomOAuth2User(user, oAuth2User.getAttributes());

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
