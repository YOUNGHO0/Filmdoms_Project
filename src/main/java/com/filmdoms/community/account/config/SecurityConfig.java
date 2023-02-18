package com.filmdoms.community.account.config;

import com.filmdoms.community.account.config.jwt.JwtAuthenticationFilter;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API는 csrf 대응 필요 없음
                .csrf().disable()

                // 세션 관리도 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // URI 인증 여부 설정
                .authorizeHttpRequests(auth -> auth
                        // localhost:8080/h2-console 사용하기 위한 설정
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/login", "/join").permitAll()
                        .requestMatchers("/api/v1/fileupload").permitAll()
                        .anyRequest().authenticated())

                // H2 DB 사용을 위해, x-frame-options 동일 출처 허용
                .headers(headers -> headers.frameOptions().disable())

                // 예외 핸들러 등록
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                // JWT 인증필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
