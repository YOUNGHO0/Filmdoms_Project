package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * 유저 ID로 계정 정보를 찾는다.
     *
     * @param username 유저 ID
     * @return 계정 정보
     */
    @Override
    public AccountDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .map(AccountDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }
}
