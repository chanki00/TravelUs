package com.DB_PASSWORD_REDACTED.trip.dto.user.enums;

import lombok.Getter;

@Getter
public enum Gender {
	M("남성"),
	F("여성"),
	O("기타");

	private final String label;

	Gender(String label) {
		this.label = label;
	}

	public static Gender fromString(String value) {
		if (value == null) return null;
		try {
			return Gender.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("해당하는 성별이 없습니다: " + value);
		}
	}
}
