package com.DB_PASSWORD_REDACTED.trip.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
	private String userId;
	private String userPw;
}
