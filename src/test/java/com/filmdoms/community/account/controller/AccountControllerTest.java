package com.filmdoms.community.account.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("정상적으로 로그인을 시도하면, 세션 ID가 담긴 쿠키를 반환한다.")
    void whenTryingToLogin_thenReturnsAccountInfo() throws Exception {
        // Given
        String username = "username";
        String password = "password";
        when(accountService.login(username, password)).thenReturn(mock(AccountDto.class));

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, password)))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JSESSIONID"));
    }

    @Test
    @DisplayName("잘못된 정보로 로그인시, 에러를 반환한다.")
    void whenTryingToLoginWith_thenReturnsErrorCode() throws Exception {
        // Given
        String username = "username";
        String password = "password";
        when(accountService.login(username, password)).thenThrow(
                new RuntimeException()
        );

        // When & Then
        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequestDto(username, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }


}
