package com.filmdoms.community.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.RefreshAccessTokenResponseDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.service.AccountService;
import java.io.IOException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AccountController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
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

    @Test
    @DisplayName("정상적으로 로그인시, 계정 정보를 반환한다.")
    void whenTryingToLogin_thenReturnsAccountInfo() throws Exception {
        // Given
        String email = "address@filmdoms.com";
        String password = "password";
        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();
        when(accountService.login(email, password)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(email, password)))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.resultCode == 'SUCCESS')]").exists())
                .andExpect(jsonPath("$..result[?(@..accessToken)]").exists())
                .andExpect(jsonPath("$..result[?(@..refreshToken)]").exists());
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
        RefreshAccessTokenResponseDto responseDto = RefreshAccessTokenResponseDto.builder()
                .accessToken("mockAccessToken")
                .build();
        when(accountService.refreshAccessToken(refreshToken)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/account/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + refreshToken)
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
                        .header("Authorization", "Bearer " + refreshToken)
                ).andDo(print())
                .andExpect(jsonPath("$.[?(@.resultCode == 'SUCCESS')]").exists());
    }

}
