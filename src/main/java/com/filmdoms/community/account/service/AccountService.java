package com.filmdoms.community.account.service;

import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 유저 ID와 비밀번호를 확인해 계정 정보를 찾는다.
     *
     * @param username 유저 ID
     * @param password 비밀번호
     * @return 계정정보
     */
    public String login(String username, String password) {
        // 가입 여부 확인
        AccountDto accountDto = accountRepository.findByUsername(username)
                .map(AccountDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호를 암호화 시켜 저장된 비밀번호와 대조
        if (!passwordEncoder.matches(password, accountDto.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 반환
        return jwtTokenProvider.createToken(accountDto.getUsername(), accountDto.getAuthorities());
    }

    public boolean isUsernameDuplicate(String username) {
        return accountRepository.existsByUsername(username);
    }

    // TODO: 회원 가입 비즈니스 로직 구현하기
    public AccountDto join(String username, String password) {
        String testUsername = (username == null) ? "tester" : username;
        String testPassword = (password == null) ? "password" : password;
        Account testAccount = Account.builder()
                .username(testUsername)
                .password(passwordEncoder.encode(testPassword))
                .role(AccountRole.USER)
                .build();
        return AccountDto.from(accountRepository.save(testAccount));
    }

}
