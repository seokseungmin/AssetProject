package com.example.assetproject.form;

import com.example.assetproject.types.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AssetSoftwareAddForm {

    private String assetCode; // 자산번호

    private Long swIdx; // 소프트웨어 인덱스
    private Long assetIdx; // 자산인덱스

    @NotBlank(message = "자산명은 필수입니다.")
    private String assetName; // 자산명

    @NotNull(message = "자산상태는 필수입니다.")
    private Status assetStatus; // 자산상태

    @NotBlank(message = "시리얼번호는 필수입니다.")
    private String sn; // 시리얼번호

    @NotBlank(message = "위치는 필수입니다.")
    private String location; // 위치

    private String dept; // 부서

    @NotNull(message = "구매일은 필수입니다.")
    private LocalDate purchaseDate; // 구매일
    private LocalDate returnDate; // 반납일
    private LocalDate assignedDate; // 지급일
    private LocalDate expiryDate; // 만료일
    private String currentUser; // 현사용자
    private String previousUser; // 전사용자

    @NotBlank(message = "제조사는 필수입니다.")
    private String manufacturer; //제조사

    private String note; // 비고
}