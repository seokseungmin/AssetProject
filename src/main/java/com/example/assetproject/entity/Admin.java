package com.example.assetproject.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자
public class Admin {

    private int adminIdx;
    private String username;
    private String password;
    private String email;
    private String role;
}
