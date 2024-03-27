package com.example.assetproject.entity;

import com.example.assetproject.types.Action;
import com.example.assetproject.types.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class History {

    private Long historyIdx;
    private String assetCode;
    private Type assetType; // enum 타입이지만 MyBatis에서는 String으로 처리
    private Action action; // 마찬가지로 String으로 처리
    private String changedBy;
    private LocalDateTime changedDate;
    private String assetJSON; // JSON 타입이지만 문자열로 처리
}
