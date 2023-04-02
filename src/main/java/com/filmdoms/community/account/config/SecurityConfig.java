package com.filmdoms.community.account.config;

import com.filmdoms.community.account.config.jwt.JwtAuthenticationFilter;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.config.oauth.CustomOAuthSuccessHandler;
import com.filmdoms.community.account.exception.CustomAccessDeniedHandler;
import com.filmdoms.community.account.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;

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
                        .requestMatchers("/api/v1/login", "/api/v1/join").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/banner").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/banner/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/banner/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").authenticated()
                        .anyRequest().permitAll())

                // H2 DB 사용을 위해, x-frame-options 동일 출처 허용
                .headers(headers -> headers.frameOptions().disable())

                // 예외 핸들러 등록
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()

                // JWT 인증필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        OAuth2LoginAuthenticationFilter.class)

                //oauth 관련 설정
                .oauth2Login()
                .successHandler(customOAuthSuccessHandler);

        return http.build();
    }
}
