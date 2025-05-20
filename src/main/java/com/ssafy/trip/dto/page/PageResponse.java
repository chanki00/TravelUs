package com.DB_PASSWORD_REDACTED.trip.dto.page;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
	 private List<T> items;
    private int totalCount;
    private int page;
    private int size;

    

}
