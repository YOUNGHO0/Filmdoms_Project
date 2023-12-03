package com.filmdoms.community.config;

import com.filmdoms.community.config.jwt.JwtAuthenticationFilter;
import com.filmdoms.community.config.jwt.JwtTokenProvider;
import com.filmdoms.community.config.oauth.CustomOAuth2AuthorizationRequestResolver;
import com.filmdoms.community.config.oauth.CustomOAuthFailureHandler;
import com.filmdoms.community.config.oauth.CustomOAuthSuccessHandler;
import com.filmdoms.community.exception.CustomAccessDeniedHandler;
import com.filmdoms.community.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuthSuccessHandler customOAuthSuccessHandler;
    private final CustomOAuthFailureHandler customOAuthFailureHandler;
    private final CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API는 csrf 대응 필요 없음
                .csrf().disable()
                .cors().and()
                // 세션 관리도 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // URI 인증 여부 설정
                .authorizeHttpRequests(auth -> auth
                        // localhost:8080/h2-console 사용하기 위한 설정
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/account/oauth").hasRole("GUEST")
                        .requestMatchers(HttpMethod.GET, "/api/v1/account/oauth/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/account/oauth/profile/email").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/account/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/account/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/account/profile/article").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/account/profile/comment").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/email/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/banner").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/banner/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/banner/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/article/announce").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasRole("ADMIN")
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
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestResolver(customOAuth2AuthorizationRequestResolver))
                        .successHandler(customOAuthSuccessHandler)
                        .failureHandler(customOAuthFailureHandler)
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("https://m.filmdoms.studio","https://film-doms.vercel.app", "http://localhost:*", "https://filmdoms.studio"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
