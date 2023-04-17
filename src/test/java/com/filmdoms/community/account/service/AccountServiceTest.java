package com.filmdoms.community.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.DeleteAccountRequestDto;
import com.filmdoms.community.account.data.dto.request.JoinRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdatePasswordRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdateProfileRequestDto;
import com.filmdoms.community.account.data.dto.response.AccountResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@WebMvcTest(
        controllers = AccountService.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("비즈니스 로직 - 회원 서비스")
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PasswordEncoder encoder;

    @Nested
    @DisplayName("로그인 기능 테스트")
    class aboutLogin {
        @Test
        @DisplayName("올바른 ID와 비밀번호를 입력하면, 유저 정보를 반환한다.")
        void givenCorrectUsernameAndPassword_whenTryingToLogin_thenReturnsAccountInfo() {
            // Given
            String username = "username";
            String password = "password";
            Account mockAccount = getMockAccount(username, password);
            when(accountRepository.findByUsername(username)).thenReturn(Optional.of(mockAccount));
            when(encoder.matches(password, mockAccount.getPassword())).thenReturn(true);
            when(jwtTokenProvider.createToken(any(), any())).thenReturn("mockJwtString");

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
            Account mockAccount = getMockAccount(username, password);
            when(accountRepository.findByUsername(username)).thenReturn(Optional.of(mockAccount));
            when(encoder.matches(password, mockAccount.getPassword())).thenReturn(false);

            // When & Then
            ApplicationException e = assertThrows(ApplicationException.class,
                    () -> accountService.login(username, password));
            assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
        }
    }

    @Nested
    @DisplayName("회원가입 기능 테스트")
    class aboutJoin {
        @Test
        @DisplayName("중복 아이디 확인시, 중복된 아이디를 입력하면, 참을 반환한다.")
        void givenDuplicateUsername_whenCheckingUsername_thenReturnsTrue() {

            // Given
            String username = "username";
            given(accountRepository.existsByUsername(username)).willReturn(true);

            // When & Then
            assertTrue(accountService.isUsernameDuplicate(username));
        }

        @Test
        @DisplayName("중복 이메일 확인시, 중복된 이메일을 입력하면, 참을 반환한다.")
        void givenDuplicateEmail_whenCheckingEmail_thenReturnsTrue() {

            // Given
            String email = "random@email.com";
            given(accountRepository.existsByUsername(email)).willReturn(true);

            // When & Then
            assertTrue(accountService.isUsernameDuplicate(email));
        }

        @Test
        @DisplayName("계정 생성시, 올바른 요청이라면, 계정을 저장한다.")
        void givenValidRequest_whenCreatingAccount_thenSavesAccount() {

            // Given
            JoinRequestDto requestDto = JoinRequestDto.builder()
                    .username("testuser")
                    .password("PassWord!0")
                    .email("random@email.com")
                    .nickname("JavaNoob")
                    .build();
            given(accountRepository.existsByUsername(any())).willReturn(false);
            given(accountRepository.existsByEmail(any())).willReturn(false);
            given(fileRepository.findById(any())).willReturn(Optional.empty());

            // When
            accountService.createAccount(requestDto);

            // Then
            then(accountRepository).should().save(any());
        }

        @Test
        @DisplayName("계정 생성시, 중복된 아이디라면, 예외를 반환한다.")
        void givenDuplicateUsername_whenCreatingAccount_thenReturnsDuplicateUsernameException() {

            // Given
            JoinRequestDto requestDto = JoinRequestDto.builder()
                    .username("testuser")
                    .password("PassWord!0")
                    .email("random@email.com")
                    .nickname("JavaNoob")
                    .build();
            given(accountRepository.existsByUsername(any())).willReturn(true);
            given(accountRepository.existsByEmail(any())).willReturn(false);
            given(fileRepository.findById(any())).willReturn(Optional.empty());

            // When
            Throwable throwable = catchThrowable(() -> accountService.createAccount(requestDto));

            // Then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.DUPLICATE_USERNAME.getMessage());
        }

        @Test
        @DisplayName("계정 생성시, 중복된 이메일이라면, 예외를 반환한다.")
        void givenDuplicateEmail_whenCreatingAccount_thenReturnsDuplicateEmailException() {

            // Given
            JoinRequestDto requestDto = JoinRequestDto.builder()
                    .username("testuser")
                    .password("PassWord!0")
                    .email("random@email.com")
                    .nickname("JavaNoob")
                    .build();
            given(accountRepository.existsByUsername(any())).willReturn(false);
            given(accountRepository.existsByEmail(any())).willReturn(true);
            given(fileRepository.findById(any())).willReturn(Optional.empty());

            // When
            Throwable throwable = catchThrowable(() -> accountService.createAccount(requestDto));

            // Then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());
        }
    }

    @Nested
    @DisplayName("프로필 수정 기능 테스트")
    class aboutUpdatingProfile {

        @Test
        @DisplayName("프로필 수정시, 올바른 요청이라면, 수정된 계정 정보를 반환한다.")
        void givenProperRequest_whenUpdatingAccountProfile_thenReturnsUpdatedAccountInformation() {

            // Given
            AccountDto mockAccountDto = mock(AccountDto.class);
            Account mockAccount = getMockAccount("testuser", "password");

            File mockNewImage = mock(File.class);
            given(mockNewImage.getId()).willReturn(2L);
            given(mockNewImage.getUuidFileName()).willReturn("randomUuidFileName");

            UpdateProfileRequestDto requestDto = UpdateProfileRequestDto.builder()
                    .fileId(2L)
                    .nickname("newNickname")
                    .build();
            given(accountRepository.findByUsernameWithImage(any())).willReturn(Optional.of(mockAccount));
            given(fileRepository.findById(any())).willReturn(Optional.of(mockNewImage));

            // When
            AccountResponseDto responseDto = accountService.updateAccountProfile(requestDto, mockAccountDto);

            // Then
            assertThat(responseDto)
                    .hasFieldOrPropertyWithValue("nickname", "newNickname");
            assertThat(responseDto.getProfileImage())
                    .hasFieldOrPropertyWithValue("id", 2L);
        }

        @Test
        @DisplayName("프로필 수정시, 요청 프로필 이미지가 없다면, 예외를 반환한다.")
        void givenInvalidFileId_whenUpdatingAccountProfile_thenReturnsInvalidImageException() {

            // Given
            AccountDto mockAccountDto = mock(AccountDto.class);
            Account mockAccount = getMockAccount("testuser", "password");

            UpdateProfileRequestDto requestDto = UpdateProfileRequestDto.builder()
                    .fileId(2L)
                    .nickname("newNickname")
                    .build();
            given(accountRepository.findByUsernameWithImage(any())).willReturn(Optional.of(mockAccount));
            given(fileRepository.findById(any())).willReturn(Optional.empty());

            // When
            Throwable throwable = catchThrowable(() -> accountService.updateAccountProfile(requestDto, mockAccountDto));

            // Then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_IMAGE_ID.getMessage());
        }
    }

    @Nested
    @DisplayName("비밀번호 수정 기능 테스트")
    class aboutUpdatingPassword {

        @Test
        @DisplayName("비밀번호 수정시, 올바른 요청이라면, 수정된 비밀번호를 저장한다.")
        void givenProperRequest_whenUpdatingAccountPassword_thenSavesUpdatedPassword() {

            // Given
            String newEncodedPassword = "newEncodedPassword";
            AccountDto mockAccountDto = mock(AccountDto.class);
            Account mockAccount = mock(Account.class);
            given(mockAccount.getPassword()).willReturn("password");

            UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.builder()
                    .oldPassword("password")
                    .newPassword("newPassword")
                    .build();
            given(accountRepository.findByUsername(any())).willReturn(Optional.of(mockAccount));
            given(encoder.matches(any(), any())).willReturn(true);
            given(encoder.encode("newPassword")).willReturn(newEncodedPassword);

            // When
            accountService.updateAccountPassword(requestDto, mockAccountDto);

            // Then
            then(mockAccount).should().updatePassword(newEncodedPassword);
        }

        @Test
        @DisplayName("비밀번호 수정시, 틀린 비밀번호를 입력하면, 예외를 반환한다.")
        void givenInvalidPassword_whenUpdatingAccountPassword_thenReturnsException() {

            // Given
            String newEncodedPassword = "newEncodedPassword";
            AccountDto mockAccountDto = mock(AccountDto.class);
            Account mockAccount = mock(Account.class);
            given(mockAccount.getPassword()).willReturn("password");

            UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.builder()
                    .oldPassword("wrongPassword")
                    .newPassword("newPassword")
                    .build();
            given(accountRepository.findByUsername(any())).willReturn(Optional.of(mockAccount));
            given(encoder.matches(any(), any())).willReturn(false);
            given(encoder.encode("newPassword")).willReturn(newEncodedPassword);

            // When
            Throwable throwable = catchThrowable(
                    () -> accountService.updateAccountPassword(requestDto, mockAccountDto));

            // Then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_PASSWORD.getMessage());
        }
    }

    @Nested
    @DisplayName("계정 삭제 기능 테스트")
    class aboutDeletingAccount {

        @Test
        @DisplayName("계정 삭제시, 올바른 요청이라면, 요청된 계정을 삭제한다.")
        void givenProperRequest_whenDeletingAccount_thenDeletesAccount() {

            // Given
            AccountDto mockAccountDto = mock(AccountDto.class);
            Account mockAccount = mock(Account.class);
            given(mockAccount.getPassword()).willReturn("password");

            DeleteAccountRequestDto requestDto = DeleteAccountRequestDto.builder()
                    .password("password")
                    .build();
            given(accountRepository.findByUsername(any())).willReturn(Optional.of(mockAccount));
            given(encoder.matches("password", "password")).willReturn(true);

            // When
            accountService.deleteAccount(requestDto, mockAccountDto);

            // Then
            then(accountRepository).should().delete(mockAccount);
        }

        @Test
        @DisplayName("계정 삭제시, 틀린 비밀번호를 입력하면, 예외를 반환한다.")
        void givenInvalidPassword_whenDeletingAccount_thenReturnsException() {

            // Given
            AccountDto mockAccountDto = mock(AccountDto.class);
            Account mockAccount = mock(Account.class);
            given(mockAccount.getPassword()).willReturn("password");

            DeleteAccountRequestDto requestDto = DeleteAccountRequestDto.builder()
                    .password("wrongPassword")
                    .build();
            given(accountRepository.findByUsername(any())).willReturn(Optional.of(mockAccount));
            given(encoder.matches("wrongPassword", "password")).willReturn(false);

            // When
            Throwable throwable = catchThrowable(
                    () -> accountService.deleteAccount(requestDto, mockAccountDto));

            // Then
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_PASSWORD.getMessage());
        }
    }

    private Account getMockAccount(String username, String password) {

        File mockOriginalImage = mock(File.class);
        given(mockOriginalImage.getId()).willReturn(1L);

        Account mockAccount = Account.builder()
                .username(username)
                .password(password)
                .nickname("oldNickname")
                .email("random@email.com")
                .role(AccountRole.USER)
                .isSocialLogin(false)
                .profileImage(mockOriginalImage)
                .build();
        ReflectionTestUtils.setField(mockAccount, Account.class, "id", 1L, Long.class);
        ReflectionTestUtils.setField(mockAccount, Account.class, "isSocialLogin", false, boolean.class);

        return mockAccount;
    }
}