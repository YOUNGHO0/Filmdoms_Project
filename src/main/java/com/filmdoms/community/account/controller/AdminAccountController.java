package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.service.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @DeleteMapping("/account/{accountId}")
    public Response deleteUserByAdmin(@PathVariable Long accountId, @AuthenticationPrincipal AccountDto accountDto) {

        adminAccountService.deleteAccount(accountId);
        return Response.success();

    }
}
