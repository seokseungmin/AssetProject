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
                        .requestMatchers("/","/login","/findUsernameForm","/findUsername","/resetPasswordForm","resetPassword").permitAll()
                        .requestMatchers("/assets/**","/register","/registerProcess","/histories").hasRole("ADMIN") //관리자만 접근가능
                        .anyRequest().authenticated() //인증을 한사람들
                );

        //커스텀 로그인 설정
        httpSecurity.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProcess")
                .defaultSuccessUrl("/assets", true) // 로그인 성공 후 이동할 URL
                .permitAll()
        );

        //개발환경에서는 CSRF 비활성화
        //CSRF(Cross-Site Request Forgery)는 요청을 위조하여 사용자가 원하지 않아도 서버측으로
        //특정 요청을 강제로 보내는 방식(회원 정보 변경, 게시글 CRUD를 사용자 모르게 요청

//        httpSecurity
//                .csrf((auth) -> auth.disable());

        //login.html에 CSRF 토큰 설정 해두면 enable가능, 로그아웃할때도 post방식으로만 로그아웃해야함!
        //API 서버의 경우 csrf.diable(), 앱에서 사용하는 API 서버의 경우 보통 세션을 STATELESS로 관리하기 때문에
        //스프링 시큐리티 csrf enable 설정을 진행하지 않아도 된다.

        httpSecurity
                .sessionManagement((auth) -> auth
                .maximumSessions(1)  //하나의 아이디에서 최대 허용할수 있는 동시접속 중복 로그인 설정
                .maxSessionsPreventsLogin(true));
        // maximumSessions의 설정값 초과시 기존의값 기존 로그인되어 있는것을 로그아웃시킬지 새로로그인되는 친구를 로그아웃 시킬지 설정


        //세션 고정보호
        httpSecurity
                .sessionManagement((auth) -> auth
                        .sessionFixation()
                        .changeSessionId());


        httpSecurity
                .logout((auth) -> auth.logoutUrl("/logout")
                .logoutSuccessUrl("/"));




        return httpSecurity.build();
    }
}
