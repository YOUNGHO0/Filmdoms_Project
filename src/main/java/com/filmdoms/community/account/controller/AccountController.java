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
import com.filmdoms.community.account.data.dto.response.RefreshAccessTokenResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
        return Response.success(accountService.login(requestDto.getEmail(), requestDto.getPassword()));
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();
    }

    @PostMapping("/refresh-token")
    public Response<RefreshAccessTokenResponseDto> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = extractToken(request)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_FOUND));
        return Response.success(accountService.refreshAccessToken(refreshToken));
    }

    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request) {
        String refreshToken = extractToken(request)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_FOUND));
        accountService.logout(refreshToken);
        return Response.success();
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
