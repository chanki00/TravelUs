package com.DB_PASSWORD_REDACTED.trip.dto.trip;

import java.util.List;

import lombok.Data;

@Data
public class Sidos {
    private int no;
    private int sidoCode;
    private String sidoName;
    
    private List<Guguns> guguns;

}
