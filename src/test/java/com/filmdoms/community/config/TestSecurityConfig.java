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
import org.springframework.test.util.ReflectionTestUtils;

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
        Account mockAdminAccount = Account.builder()
                .username("testAdmin")
                .password("password")
                .role(AccountRole.ADMIN)
                .build();
        ReflectionTestUtils.setField(mockAdminAccount, Account.class, "id", 1L, Long.class);
        given(accountRepository.findByUsername("testAdmin")).willReturn(Optional.of(mockAdminAccount));

        Account mockUserAccount = Account.builder()
                .username("testUser")
                .password("password")
                .role(AccountRole.USER)
                .build();
        ReflectionTestUtils.setField(mockUserAccount, Account.class, "id", 2L, Long.class);
        given(accountRepository.findByUsername("testUser")).willReturn(Optional.of(mockUserAccount));
    }

}