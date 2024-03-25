package com.example.assetproject.form;


import com.example.assetproject.types.Status;
import com.example.assetproject.types.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AssetHardwareAddForm {

    private String assetCode; // 자산번호

    private Long assetIdx; // 자산인덱스

    @NotBlank(message = "자산명은 필수입니다.")
    private String assetName; // 자산명

    @NotNull(message = "자산구분은 필수입니다.")
    private Type assetType; // 자산구분

    @NotNull(message = "자산상태는 필수입니다.")
    private Status assetStatus; // 자산상태

    @NotBlank(message = "시리얼번호는 필수입니다.")
    private String sn; // 시리얼번호

    @NotBlank(message = "위치는 필수입니다.")
    private String location; // 위치

    private String dept; // 부서

    @NotNull(message = "구매일은 필수입니다.")
    private LocalDate purchaseDate; // 구매일

    private LocalDate assignedDate; // 지급일
    private LocalDate returnDate; // 반납일
    private String currentUser; // 현사용자
    private String previousUser; // 전사용자

    @NotBlank(message = "제조사는 필수입니다.")
    private String manufacturer; //제조사

    private String cpu; // 중앙처리장치
    private String ssd; // 스토리지
    private String hdd; // 하드디스크
    private String memory; // 메모리

    private String usageDuration; // 사용기간
    private String note; // 비고
}