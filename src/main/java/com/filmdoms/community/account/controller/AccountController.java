package com.filmdoms.community.account.controller;

import com.filmdoms.community.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.LoginDto;
import com.filmdoms.community.account.data.dto.request.*;
import com.filmdoms.community.account.data.dto.request.profile.UpdateFavoriteMoviesDto;
import com.filmdoms.community.account.data.dto.request.profile.UpdateNicknameRequestDto;
import com.filmdoms.community.account.data.dto.request.profile.UpdateProfileImageRequestDto;
import com.filmdoms.community.account.data.dto.response.AccessTokenResponseDto;
import com.filmdoms.community.account.data.dto.response.AccountResponseDto;
import com.filmdoms.community.account.data.dto.response.CheckDuplicateResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.dto.response.profile.ProfileArticleResponseDto;
import com.filmdoms.community.account.data.dto.response.profile.ProfileCommentResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.AccountService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
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
    public Response<AccessTokenResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        LoginDto dto = accountService.login(requestDto.getEmail(), requestDto.getPassword());
        ResponseCookie cookie = jwtTokenProvider.createRefreshTokenCookie(dto.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return Response.success(AccessTokenResponseDto.from(dto));
    }

    private Optional<String> extractRefreshToken(String cookieHeader) {
        return Optional.ofNullable(cookieHeader).flatMap(header -> Arrays.stream(header.split("; "))
                .filter(cookie -> cookie.startsWith("refreshToken="))
                .findFirst()
                .map(cookie -> cookie.substring("refreshToken=".length())));
    }

    @PostMapping("/refresh-token")
    public Response<AccessTokenResponseDto> refreshAccessToken(@CookieValue("refreshToken") String refreshToken) {
        return Response.success(accountService.refreshAccessToken(refreshToken));
    }

    @PostMapping("/logout")
    public Response<Void> logout(@CookieValue("refreshToken") String refreshToken) {
        accountService.logout(refreshToken);
        return Response.success();
    }

    @PostMapping()
    public Response<AccessTokenResponseDto> join(@RequestBody JoinRequestDto requestDto, HttpServletResponse response) {
        accountService.createAccount(requestDto);
        LoginDto dto = accountService.login(requestDto.getEmail(), requestDto.getPassword());
        ResponseCookie cookie = jwtTokenProvider.createRefreshTokenCookie(dto.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return Response.success(AccessTokenResponseDto.from(dto));
    }

    @PostMapping("/oauth")
    public Response addInformationToSocialLoginAccount(@RequestBody @Valid OAuthJoinRequestDto requestDto, BindingResult bindingResult, @AuthenticationPrincipal AccountDto accountDto) throws MethodArgumentNotValidException {
        List<String> favoriteMovies = requestDto.getFavoriteMovies();
        if (duplicateExistsInMovieNameList(favoriteMovies)) {
            bindingResult.rejectValue("favoriteMovies", "duplicate");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        accountService.addInformationToSocialLoginAccount(requestDto, accountDto);
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

//    @PutMapping("/profile")
//    public Response<Void> updateProfile(
//            @RequestBody UpdateProfileRequestDto requestDto,
//            @AuthenticationPrincipal AccountDto accountDto) {
//        accountService.updateAccountProfile(requestDto, accountDto);
//        return Response.success();
//    }

    @PutMapping("/profile/nickname")
    public Response<Void> updateNickname(
            @RequestBody UpdateNicknameRequestDto requestDto,
            @AuthenticationPrincipal AccountDto accountDto) {

        accountService.updateNickname(requestDto, accountDto);
        return Response.success();
    }

    @PutMapping("/profile/favoritemovie")
    public Response<AccountResponseDto> updateFavoriteMovie(
            @RequestBody UpdateFavoriteMoviesDto requestDto,
            @AuthenticationPrincipal AccountDto accountDto) {

        return Response.success(accountService.updateFavoriteMovie(requestDto, accountDto));

    }

    @PutMapping("/profile/profileimage")
    public Response<Void> updateProfileImage(
            @RequestBody UpdateProfileImageRequestDto requestDto,
            @AuthenticationPrincipal AccountDto accountDto) {

        accountService.updateProfileImage(requestDto, accountDto);
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

    private boolean duplicateExistsInMovieNameList(List<String> favoriteMovies) {
        return favoriteMovies.size() != favoriteMovies.stream().distinct().count();
    }
}
