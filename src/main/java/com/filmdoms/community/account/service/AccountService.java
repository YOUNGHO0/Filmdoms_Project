package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 ID로 계정 정보를 찾는다.
     * 로그인 된 세션 ID로 유저ID를 찾아 계정 정보를 얻을 때 사용한다.
     * @param username 유저 ID
     * @return 계정 정보
     */
    public AccountDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .map(AccountDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 유저 ID와 비밀번호를 확인해 계정 정보를 찾는다.
     * @param username 유저 ID
     * @param password 비밀번호
     * @return 계정정보
     */
    public AccountDto login(String username, String password) {
        // 가입 여부 확인
        AccountDto accountDto = loadUserByUsername(username);

        // 비밀번호를 암호화 시켜 저장된 비밀번호와 대조
        if (!passwordEncoder.matches(password, accountDto.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 계정 정보 반환
        return accountDto;
    }

    // TODO: 회원 가입 비즈니스 로직 구현하기
    public AccountDto join() {
        String testUsername = "tester";
        String testPassword = "password";
        Account testAccount = Account.of(testUsername, passwordEncoder.encode(testPassword));
        return AccountDto.from(accountRepository.save(testAccount));
    }

}
