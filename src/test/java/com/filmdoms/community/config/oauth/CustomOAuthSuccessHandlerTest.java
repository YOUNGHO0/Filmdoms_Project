package com.filmdoms.community.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.OAuthType;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.RefreshTokenRepository;
import com.filmdoms.community.account.service.TokenAuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        JwtTokenProvider.class,
        ObjectMapper.class
})
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "JWT_KEY=aKeyThatIsVeryLongToBeUsedForJWTKEY"
})
@DisplayName("소셜 로그인 로직 테스트")
class CustomOAuthSuccessHandlerTest {

    @SpyBean
    CustomOAuthSuccessHandler customOAuthSuccessHandler;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    @MockBean
    TokenAuthenticationService tokenAuthenticationService;

    private static final String ACCESS_TOKEN_CAMEL_CASE = "accessToken";
    private static final String REFRESH_TOKEN_CAMEL_CASE = "refreshToken";

    @Test
    @DisplayName("신규 이메일의 경우, 계정을 생성하고, 액세스, 리프레시 토큰을 발급하며, SIGNUP 응답이 나감")
    void newEmail() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authentication = null;
        String testEmail = "testEmail@filmdoms.com";
        Account mockAccount = createMockAccount(testEmail, AccountRole.GUEST, true);

        doReturn(testEmail).when(customOAuthSuccessHandler).resolveEmailFromAuthentication(authentication);
        doReturn(Optional.empty()).when(refreshTokenRepository).findByKey(any());
        doReturn(Optional.empty()).when(accountRepository).findByEmail(testEmail);
        doReturn(mockAccount).when(accountRepository).save(any());

        //when
        customOAuthSuccessHandler.handle(request, response, authentication);

        //then
        String responseContent = response.getContentAsString();
        verify(accountRepository).save(any());
        assertThat(responseContent).contains(ACCESS_TOKEN_CAMEL_CASE);
        assertThat(response.getCookie(REFRESH_TOKEN_CAMEL_CASE)).isNotNull();
        assertThat(responseContent).contains(OAuthType.SIGNUP.name());
    }

    @Test
    @DisplayName("GUEST 등급 소셜 로그인 계정의 경우, 액세스, 리프레시 토큰을 발급하며, SIGNUP 응답이 나감")
    void guestRoleAccount() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authentication = null;
        String testEmail = "testEmail@filmdoms.com";
        Account mockAccount = createMockAccount(testEmail, AccountRole.GUEST, true);

        doReturn(testEmail).when(customOAuthSuccessHandler).resolveEmailFromAuthentication(authentication);
        doReturn(Optional.empty()).when(refreshTokenRepository).findByKey(any());
        doReturn(Optional.ofNullable(mockAccount)).when(accountRepository).findByEmail(testEmail);

        //when
        customOAuthSuccessHandler.handle(request, response, authentication);

        //then
        String responseContent = response.getContentAsString();
        verify(accountRepository, times(0)).save(any());
        assertThat(responseContent).contains(ACCESS_TOKEN_CAMEL_CASE);
        assertThat(response.getCookie(REFRESH_TOKEN_CAMEL_CASE)).isNotNull();
        assertThat(responseContent).contains(OAuthType.SIGNUP.name());
    }

    @Test
    @DisplayName("USER 등급 소셜 로그인 계정의 경우, 액세스, 리프레시 토큰을 발급하며, LOGIN 응답이 나감")
    void userRoleAccount() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authentication = null;
        String testEmail = "testEmail@filmdoms.com";
        Account mockAccount = createMockAccount(testEmail, AccountRole.USER, true);

        doReturn(testEmail).when(customOAuthSuccessHandler).resolveEmailFromAuthentication(authentication);
        doReturn(Optional.empty()).when(refreshTokenRepository).findByKey(any());
        doReturn(Optional.ofNullable(mockAccount)).when(accountRepository).findByEmail(testEmail);

        //when
        customOAuthSuccessHandler.handle(request, response, authentication);

        //then
        String responseContent = response.getContentAsString();
        verify(accountRepository, times(0)).save(any());
        assertThat(responseContent).contains(ACCESS_TOKEN_CAMEL_CASE);
        assertThat(response.getCookie(REFRESH_TOKEN_CAMEL_CASE)).isNotNull();
        assertThat(responseContent).contains(OAuthType.LOGIN.name());
    }

    @Test
    @DisplayName("소셜 로그인 계정이 아닌 경우 에러 응답 발생")
    void NotSocialLoginAccount() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authentication = null;
        String testEmail = "testEmail@filmdoms.com";
        Account mockAccount = createMockAccount(testEmail, AccountRole.USER, false);

        doReturn(testEmail).when(customOAuthSuccessHandler).resolveEmailFromAuthentication(authentication);
        doReturn(Optional.empty()).when(refreshTokenRepository).findByKey(any());
        doReturn(Optional.ofNullable(mockAccount)).when(accountRepository).findByEmail(testEmail);

        //when & then
        customOAuthSuccessHandler.handle(request, response, authentication);
        String responseContent = response.getContentAsString();
        assertThat(responseContent).contains(ErrorCode.NOT_SOCIAL_LOGIN_ACCOUNT.name());
    }

    private Account createMockAccount(String email, AccountRole role, boolean isSocialLogin) {
        return Account.builder()
                .email(email)
                .role(role)
                .isSocialLogin(isSocialLogin)
                .build();
    }
}