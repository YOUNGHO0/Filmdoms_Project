package com.filmdoms.community.config;

import static org.mockito.BDDMockito.given;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.UserDetailsServiceImpl;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import({SecurityConfig.class, JwtTokenProvider.class, UserDetailsServiceImpl.class})
@TestPropertySource(properties = {
        "JWT_KEY=aKeyThatIsVeryLongToBeUsedForJWTKEY"
})
public class TestSecurityConfig {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserDetailsService userDetailsService;
    @MockBean
    private AccountRepository accountRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(accountRepository.findByUsername("testUser")).willReturn(
                Optional.of(Account.of(1L, "testUser", "password", AccountRole.USER)));
        given(accountRepository.findByUsername("testAdmin")).willReturn(
                Optional.of(Account.of(2L, "testAdmin", "password", AccountRole.ADMIN)));
    }

}