package com.DB_PASSWORD_REDACTED.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class TagDTO {
	private int id;
	private String name;
	private TagType type;

    public enum TagType {
        성격, 여행
    }
}
