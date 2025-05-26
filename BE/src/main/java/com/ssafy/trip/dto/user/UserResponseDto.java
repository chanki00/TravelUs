package com.DB_PASSWORD_REDACTED.trip.dto.user;

import java.time.LocalDateTime;

import com.DB_PASSWORD_REDACTED.trip.dto.user.enums.Gender;
import com.DB_PASSWORD_REDACTED.trip.dto.user.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
	private int id;
	private String userId;
	private String userEmail;
	private String name;
	private String age;
	private Gender gender;
	private String address;
	private String intro;
	private Role role;
	private Boolean allowInvite;
}
