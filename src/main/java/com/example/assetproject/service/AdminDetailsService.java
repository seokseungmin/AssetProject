package com.example.assetproject.service;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.entity.Admin;
import com.example.assetproject.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByUsername(username);

        if(admin != null){
            return new AdminUserDetails(admin);
        }

        return null;
    }

    public String findUsernameByEmail(String email) {
        return adminRepository.findUsernameByEmail(email);
    }


    public void resetPasswordAndSendEmail(String username, String email) {
        Admin admin = adminRepository.findByUsernameAndEmail(username, email);
        log.error("admin={}",admin);
        System.out.println("username = " + username + ", email = " + email);
        System.out.println("admin = " + admin);
        if (admin != null) {
            String newPassword = generateRandomPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            admin.setPassword(encodedPassword);
            adminRepository.updatePassword(admin);

            // 이메일 전송
            // 이메일 내용 생성
            String emailContent = "<h1>Your New Password</h1><p>Here is your new password: " + newPassword + "</p>";
            try {
                // 이메일 발송
                emailService.sendEmail(email, "Your New Password", emailContent);
            } catch (Exception e) {
                log.error("Email send failed", e);
            }
        }

    }

    private String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = upperCaseLetters.toLowerCase();
        String numbers = "0123456789";
        String specialCharacters = "!@#$%";
        String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // 각 조건별로 최소 1개의 문자를 랜덤으로 추가
        sb.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        sb.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        sb.append(numbers.charAt(random.nextInt(numbers.length())));
        sb.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        // 나머지는 combinedChars에서 랜덤으로 선택하여 추가
        for (int i = 4; i < 12; i++) { // 최소 길이를 8로 설정했으나, 더 긴 비밀번호를 원한다면 길이 조정
            sb.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return sb.toString();
    }

}
