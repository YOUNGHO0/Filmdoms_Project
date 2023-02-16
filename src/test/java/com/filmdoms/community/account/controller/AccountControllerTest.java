package com.filmdoms.community.account.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = AccountController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
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
        String username = "username";
        String password = "password";
        when(accountService.login(username, password)).thenReturn("mockJwtString");

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, password)))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.resultCode == 'SUCCESS')]").exists())
                .andExpect(jsonPath("$..result[?(@..accessToken)]").exists());
    }

    @Test
    @DisplayName("잘못된 ID로 로그인시, 에러 코드를 반환한다.")
    void whenTryingToLoginWithWrongId_thenReturnsIsNotFound() throws Exception {
        // Given
        String username = "wrong_username";
        String password = "password";
        when(accountService.login(username, password)).thenThrow(
                new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, password)))
                ).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.[?(@.resultCode == 'USER_NOT_FOUND')]").exists());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인시, 에러 코드를 반환한다.")
    void whenTryingToLoginWithWrongPassword_thenReturnsIsUnauthorized() throws Exception {
        // Given
        String username = "username";
        String password = "wrong_password";
        when(accountService.login(username, password)).thenThrow(
                new ApplicationException(ErrorCode.INVALID_PASSWORD)
        );

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.[?(@.resultCode == 'INVALID_PASSWORD')]").exists());
    }
}
