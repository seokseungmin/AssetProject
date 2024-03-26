package com.example.assetproject.controller;

import com.example.assetproject.dto.RegisterDTO;
import com.example.assetproject.entity.Admin;
import com.example.assetproject.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("admin", new Admin());
        return"user/register";
    }

    @PostMapping("/registerProcess")
    public String registerProcess(@Validated @ModelAttribute("admin") RegisterDTO registerDTO, BindingResult bindingResult) {

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {} ", bindingResult);
            bindingResult.addError(new ObjectError("admin",new String[]{"adminEnroll"}, null,null));
            return "user/register";
        }

        String registerResult = registerService.save(registerDTO);

        switch (registerResult) {
            case "username.exists":
                bindingResult.rejectValue("username", "username.exists", "이미 존재하는 사용자 이름입니다.");
                break;
            case "email.exists":
                bindingResult.rejectValue("email", "email.exists", "이미 존재하는 이메일입니다.");
                break;
            case "password.invalid":
                bindingResult.rejectValue("password", "password.invalid", "비밀번호는 8자 이상이며, 최소 하나의 숫자와 특수문자를 포함해야 합니다.");
                break;
        }

        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        //회원가입 성공시 로그인 페이지로
        return "redirect:/login";
    }
}
