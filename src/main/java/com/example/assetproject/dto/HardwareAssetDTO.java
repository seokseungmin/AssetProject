package com.example.assetproject.dto;

import com.example.assetproject.types.Status;
import com.example.assetproject.types.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class HardwareAssetDTO {
    // Asset 테이블 필드
    private Long assetIdx;
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

    // Hardware 테이블 필드
    private Long hardwareIdx;
    private String cpu;
    private String ssd;
    private String hdd;
    private String memory;
    private LocalDate usageDuration;
    private String note;
}
