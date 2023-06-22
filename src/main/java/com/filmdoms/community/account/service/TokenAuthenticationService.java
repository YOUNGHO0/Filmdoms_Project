package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationService {

    private final AccountRepository accountRepository;

    /**
     * 유저 ID로 계정 정보를 찾는다.
     *
     * @param subject 유저 ID
     * @return 계정 정보
     */
    public AccountDto findAccountBySubject(String subject) {
        Long accountId;
        try {
            accountId = Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        }
        return accountRepository.findById(accountId)
                .map(AccountDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }
}
