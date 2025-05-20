package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private @NonNull String id;
	private @NonNull String pw;
	private @NonNull String name;
	private @NonNull String email;
	private String role;
	
	
}
