package com.example.assetproject.dto;


import com.example.assetproject.types.Status;
import com.example.assetproject.types.Type;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자
@EqualsAndHashCode
public class Asset {
    private Long assetIdx; // 고유 식별자로 충분
    private String assetCode;
    private String assetName;
    private Type assetType;
    private Status assetStatus;
    private String sn;
    private String location;
    private String dept;
    private LocalDate purchaseDate;
    private LocalDate assignedDate;
    private LocalDate returnDate;
    private String currentUser;
    private String previousUser;
    private String manufacturer;
}

