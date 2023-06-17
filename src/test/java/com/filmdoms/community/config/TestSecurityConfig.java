package com.filmdoms.community.config;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.config.oauth.CustomOAuth2AuthorizationRequestResolver;
import com.filmdoms.community.account.config.oauth.CustomOAuthFailureHandler;
import com.filmdoms.community.account.config.oauth.CustomOAuthSuccessHandler;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.TokenAuthenticationService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@Import({SecurityConfig.class, JwtTokenProvider.class, TokenAuthenticationService.class})
@TestPropertySource(properties = {
        "JWT_KEY=aKeyThatIsVeryLongToBeUsedForJWTKEY"
})
public class TestSecurityConfig {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CustomOAuthSuccessHandler customOAuthSuccessHandler;

    @MockBean
    CustomOAuthFailureHandler customOAuthFailureHandler;

    @MockBean
    CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;

    @BeforeTestMethod
    public void securitySetUp() {
        Account mockAdminAccount = Account.builder()
                .email("testAdmin@filmdoms.com")
                .password("password")
                .role(AccountRole.ADMIN)
                .build();
        ReflectionTestUtils.setField(mockAdminAccount, Account.class, "id", 1L, Long.class);
        given(accountRepository.findByEmail("testAdmin@filmdoms.com")).willReturn(Optional.of(mockAdminAccount));

        Account mockUserAccount = Account.builder()
                .email("testUser@filmdoms.com")
                .password("password")
                .role(AccountRole.USER)
                .build();
        ReflectionTestUtils.setField(mockUserAccount, Account.class, "id", 2L, Long.class);
        given(accountRepository.findByEmail("testUser@filmdoms.com")).willReturn(Optional.of(mockUserAccount));
    }

}