package com.example.assetproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //이 클래스가 스프링 시큐리티한테도 관리됨
public class SecurityConfig {

    //암호화 진행 메서드
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login","/register","/registerProcess").permitAll()
                        .requestMatchers("/assets/**").hasRole("ADMIN") //관리자만 접근가능
                        .anyRequest().authenticated() //인증을 한사람들
                );

        //커스텀 로그인 설정
        httpSecurity.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProcess")
                .permitAll()
        );

        //csrf 비활성화
        httpSecurity.
                csrf((auth) -> auth.disable());

        return httpSecurity.build();
    }
}
