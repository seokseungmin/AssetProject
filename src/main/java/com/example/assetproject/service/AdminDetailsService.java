package com.example.assetproject.service;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.entity.Admin;
import com.example.assetproject.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

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

    public Admin findByUsernameAndEmail(String username, String email) {
         return adminRepository.findByUsernameAndEmail(username, email);
    }

}
