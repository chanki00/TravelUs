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
public class UserInfoDto {
	private int id;
	private @NonNull String name;
	private @NonNull String userEmail;
	private String address;
	private String age;
	private Gender gender;
	private String intro;
	private Boolean allowInvite;
}
