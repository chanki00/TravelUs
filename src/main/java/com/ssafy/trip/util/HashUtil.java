package com.DB_PASSWORD_REDACTED.trip.util;

public class HashUtil {
	private static long hash(String id, String pw) {
		int stretch = 5;

		long hash = 1;
		String combined = id + pw;

		for (int i = 0; i < stretch; ++i) {
			for (int j = 0; j < combined.length(); ++j) {
				hash ^= combined.charAt(j);
				hash *= 31;
			}
			
			combined = Long.toHexString(hash);
		}

		return hash & 0x7FFFFFFFFFFFFFFFL;
	}
	
	public static String hashToString(String id, String pw) {
		long hash = hash(id, pw);
		return Long.toHexString(hash);
	}
}
