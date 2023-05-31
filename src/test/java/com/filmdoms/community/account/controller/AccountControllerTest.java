package com.filmdoms.community.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.dto.LoginDto;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.AccessTokenResponseDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.AccountService;
import java.util.List;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AccountController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@ActiveProfiles("test")
@DisplayName("컨트롤러 - 회원 서비스")
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("정상적으로 로그인시, 계정 정보를 반환한다.")
    void whenTryingToLogin_thenReturnsAccountInfo() throws Exception {
        // Given
        String email = "address@filmdoms.com";
        String password = "password";
        LoginDto responseDto = LoginDto.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();
        when(jwtTokenProvider.createRefreshTokenCookie("mockRefreshToken"))
                .thenReturn(ResponseCookie.from("refreshToken", "mockRefreshToken")
                        .httpOnly(true)
                        .maxAge(1000L * 60 * 60)
                        .build());
        when(accountService.login(email, password)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(email, password)))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.resultCode == 'SUCCESS')]").exists())
                .andExpect(jsonPath("$..result[?(@..accessToken)]").exists())
                .andExpect(result -> {
                    List<String> setCookieHeaders = result.getResponse().getHeaders(HttpHeaders.SET_COOKIE);
                    assertThat(setCookieHeaders).isNotNull().hasSize(1);
                    String setCookieHeaderValue = setCookieHeaders.get(0);
                    assertThat(setCookieHeaderValue).contains("refreshToken");
                    assertThat(setCookieHeaderValue).contains("HttpOnly");
                    assertThat(setCookieHeaderValue).contains("Max-Age");
                });
    }

    @Test
    @DisplayName("잘못된 이메일로 로그인시, 에러 코드를 반환한다.")
    void whenTryingToLoginWithWrongId_thenReturnsIsNotFound() throws Exception {
        // Given
        String email = "wrong_address@filmdoms.com";
        String password = "password";
        when(accountService.login(email, password)).thenThrow(
                new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(email, password)))
                ).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[?(@.resultCode == 'USER_NOT_FOUND')]").exists());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인시, 에러 코드를 반환한다.")
    void whenTryingToLoginWithWrongPassword_thenReturnsIsUnauthorized() throws Exception {
        // Given
        String email = "address@filmdoms.com";
        String password = "wrong_password";
        when(accountService.login(email, password)).thenThrow(
                new ApplicationException(ErrorCode.INVALID_PASSWORD)
        );

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(email, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.[?(@.resultCode == 'INVALID_PASSWORD')]").exists());
    }

    @Test
    @DisplayName("액세스 토큰 재발급시, 올바른 리프레시 토큰을 헤더에 포함해 요청하면, 새로운 액세스 토큰을 발급한다.")
    void whenRefreshingAccessToken_thenReturnsNewAccessToken() throws Exception {
        // Given
        String refreshToken = "mockRefreshToken";
        AccessTokenResponseDto responseDto = AccessTokenResponseDto.builder()
                .accessToken("mockAccessToken")
                .build();
        when(accountService.refreshAccessToken(refreshToken)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/account/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print())
                .andExpect(jsonPath("$.[?(@.resultCode == 'SUCCESS')]").exists())
                .andExpect(jsonPath("$..result[?(@..accessToken)]").exists());
    }

    @Test
    @DisplayName("로그 아웃시, 올바른 리프레시 토큰을 헤더에 포함해 요청하면, 성공 코드를 반환한다.")
    void given_properRefreshToken_whenLoggingOut_thenReturnsNewAccessToken() throws Exception {

        // Given
        String refreshToken = "mockRefreshToken";

        // When & Then
        mockMvc.perform(post("/api/v1/account/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print())
                .andExpect(jsonPath("$.[?(@.resultCode == 'SUCCESS')]").exists());
    }

}
