package com.example.assetproject.controller;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.service.AdminDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminDetailsService adminDetailsService;

    //로그인한 관리자 정보 보기
    @GetMapping("/admin/detail")
    public String main(Model model){

        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        Object principal = authentication.getPrincipal();

        if (principal instanceof AdminUserDetails) {
            int adminIdx = ((AdminUserDetails) principal).getIdx();
            String email = ((AdminUserDetails) principal).getEmail();
            String password = ((AdminUserDetails) principal).getPassword();
            String username = ((AdminUserDetails) principal).getUsername();
            model.addAttribute("adminIdx", adminIdx);
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            model.addAttribute("email", email);
        }

        model.addAttribute("id", id);
        model.addAttribute("role", role);
        return "user/admin";
    }

    //사용자 아이디/이름 찾기
    @GetMapping("/findUsernameForm")
    public String showFindUsernameForm() {
        return "user/findUsernameForm"; // 사용자 이름 입력 폼 페이지
    }

    @PostMapping("/findUsername")
    public String findUsernameByEmail(@RequestParam("email") String email, Model model) {
        String username = adminDetailsService.findUsernameByEmail(email);
        model.addAttribute("username", username);
        return "user/usernameResult"; // 이메일로 찾은 사용자 이름을 보여주는 페이지
    }


}
