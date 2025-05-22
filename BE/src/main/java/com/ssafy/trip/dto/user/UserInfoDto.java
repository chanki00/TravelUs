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
	private Long id;
	private @NonNull String name;
	private @NonNull String userEmail;
	private @NonNull String address;
	private @NonNull String age;
}
