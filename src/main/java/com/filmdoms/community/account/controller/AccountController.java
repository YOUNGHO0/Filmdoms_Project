package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.DeleteAccountRequestDto;
import com.filmdoms.community.account.data.dto.request.JoinRequestDto;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdatePasswordRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdateProfileRequestDto;
import com.filmdoms.community.account.data.dto.response.AccountResponseDto;
import com.filmdoms.community.account.data.dto.response.CheckDuplicateResponseDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/login")
    public Response<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {

        // 로그인 확인
        String accessToken = accountService.login(requestDto.getEmail(), requestDto.getPassword());

        // JWT 토큰 반환
        return Response.success(new LoginResponseDto(accessToken));
    }

    @PostMapping()
    public Response<Void> join(@RequestBody JoinRequestDto requestDto) {
        accountService.createAccount(requestDto);
        return Response.success();
    }

    @GetMapping("/check/nickname")
    public Response<CheckDuplicateResponseDto> isUsernameDuplicate(@RequestParam String nickname) {
        return Response.success(new CheckDuplicateResponseDto(accountService.isNicknameDuplicate(nickname)));
    }

    @GetMapping("/check/email")
    public Response<CheckDuplicateResponseDto> isEmailDuplicate(@RequestParam String email) {
        return Response.success(new CheckDuplicateResponseDto(accountService.isEmailDuplicate(email)));
    }

    @GetMapping("/profile")
    public Response<AccountResponseDto> viewProfile(@AuthenticationPrincipal AccountDto accountDto) {
        return Response.success(accountService.readAccount(accountDto));
    }

    @PutMapping("/profile")
    public Response<Void> updateProfile(
            @RequestBody UpdateProfileRequestDto requestDto,
            @AuthenticationPrincipal AccountDto accountDto) {
        accountService.updateAccountProfile(requestDto, accountDto);
        return Response.success();
    }

    @PutMapping("/profile/password")
    public Response<Void> updatePassword(
            @RequestBody UpdatePasswordRequestDto requestDto,
            @AuthenticationPrincipal AccountDto accountDto) {
        accountService.updateAccountPassword(requestDto, accountDto);
        return Response.success();
    }

    @DeleteMapping()
    public Response<Void> deleteAccount(
            @RequestBody DeleteAccountRequestDto requestDto,
            @AuthenticationPrincipal AccountDto accountDto) {
        accountService.deleteAccount(requestDto, accountDto);
        return Response.success();
    }

}
