package com.DB_PASSWORD_REDACTED.trip.dto.user;

import java.time.LocalDateTime;

import com.DB_PASSWORD_REDACTED.trip.dto.user.enums.Gender;
import com.DB_PASSWORD_REDACTED.trip.dto.user.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
	private @NonNull String userId;
	private @NonNull String userPw;
	private @NonNull String UserEmail;
	private @NonNull String name;
	private @NonNull Integer age;
	private @NonNull Gender gender;
	private @NonNull String address;
	private String intro;
	private Role role;
	private Boolean allow_invite;
	private LocalDateTime createdAt;
}
