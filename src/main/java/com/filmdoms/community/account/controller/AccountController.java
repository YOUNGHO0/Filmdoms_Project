package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.*;
import com.filmdoms.community.account.data.dto.response.*;
import com.filmdoms.community.account.data.dto.response.profile.ProfileArticleResponseDto;
import com.filmdoms.community.account.data.dto.response.profile.ProfileCommentResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    @Value("${admin-password}")
    private String password;

    @GetMapping("/temp/admin")
    public Response generateAdmin() {
        Account account = Account.builder()
                .nickname("admin")
                .password(passwordEncoder.encode(password))
                .role(AccountRole.ADMIN)
                .email("testadmin@naver.com")
                .build();
        accountRepository.save(account);
        return Response.success();
    }

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

    @GetMapping("/profile/{accountId}/article")
    public Response<ProfileArticleResponseDto> getProfileArticles(@PathVariable Long accountId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ProfileArticleResponseDto responseDto = accountService.getProfileArticles(accountId, pageable);
        return Response.success(responseDto);
    }

    @GetMapping("/profile/{accountId}/comment")
    public Response<ProfileCommentResponseDto> getProfileComments(@PathVariable Long accountId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ProfileCommentResponseDto responseDto = accountService.getProfileComments(accountId, pageable);
        return Response.success(responseDto);
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
