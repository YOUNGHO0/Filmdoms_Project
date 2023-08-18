package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public Response deleteAccount(Long accountId) {
        Optional<Account> optionalUserAccount = accountRepository.findById(accountId);
        if (optionalUserAccount.isPresent()) {
            accountService.deleteExpiredAccount(optionalUserAccount.get());
            return Response.success();
        }
        return Response.error(ErrorCode.USER_NOT_EXIST.getMessage());
    }

}
