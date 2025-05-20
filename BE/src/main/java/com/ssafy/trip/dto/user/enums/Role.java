package com.DB_PASSWORD_REDACTED.trip.dto.user.enums;

public enum Role {
	ADMIN("관리자"), USER("사용자");
	
	private final String label;
	
	Role(String label) {
		this.label = label;
	}
	
	public static Role fromString(String value) {
		if (value == null) return null;
		try {
			return Role.valueOf(value.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new RuntimeException("해당하는 권한이 없습니다." + value);
		}
	}

	@Override
	public String toString() {
		return label;
	}
}
