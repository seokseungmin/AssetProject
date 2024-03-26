package com.example.assetproject.service;

import com.example.assetproject.dto.RegisterDTO;
import com.example.assetproject.entity.Admin;
import com.example.assetproject.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$";
        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null) return false;
        return pat.matcher(password).matches();
    }

    public String save(RegisterDTO registerDTO) {
        if (adminRepository.existsByUsername(registerDTO.getUsername())) {
            return "username.exists"; // 이미 존재하는 사용자 이름
        }

        if (!isValidPassword(registerDTO.getPassword())) {
            return "password.invalid"; // 비밀번호 검증 실패
        }

        if (adminRepository.existsByEmail(registerDTO.getEmail())) {
            return "email.exists"; // 이미 존재하는 이메일
        }

        Admin admin = new Admin();
        admin.setUsername(registerDTO.getUsername());
        //해시화 암호화 과정
        admin.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));
        admin.setEmail(registerDTO.getEmail());
        admin.setRole("ROLE_ADMIN");

        adminRepository.save(admin);
        return "success";
    }
}
