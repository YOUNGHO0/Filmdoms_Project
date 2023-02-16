package com.filmdoms.community.account.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("비즈니스 로직 - 회원 서비스")
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PasswordEncoder encoder;

    @Test
    @DisplayName("올바른 ID와 비밀번호를 입력하면, 유저 정보를 반환한다.")
    void givenCorrectUsernameAndPassword_whenTryingToLogin_thenReturnsAccountInfo() {
        // Given
        String username = "username";
        String password = "password";
        Account mockAccount = makeMockAccount(username, password);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(mockAccount));
        when(encoder.matches(password, mockAccount.getPassword())).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> accountService.login(username, password));
    }

    @Test
    @DisplayName("존재하지 않는 ID를 입력하면, 예외를 반환한다.")
    void givenWrongUsername_whenTryingToLogin_thenReturnsException() {
        // Given
        String username = "wrong_username";
        String password = "password";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        ApplicationException e = assertThrows(ApplicationException.class,
                () -> accountService.login(username, password));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("일치하지 않는 비밀번호를 입력하면, 예외를 반환한다.")
    void givenWrongPassword_whenTryingToLogin_thenReturnsException() {
        // Given
        String username = "username";
        String password = "wrong_password";
        Account mockAccount = makeMockAccount(username, password);
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(mockAccount));
        when(encoder.matches(password, mockAccount.getPassword())).thenReturn(false);

        // When & Then
        ApplicationException e = assertThrows(ApplicationException.class,
                () -> accountService.login(username, password));
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }

    private Account makeMockAccount(String username, String password) {
        return Account.of(username, password);
    }

}