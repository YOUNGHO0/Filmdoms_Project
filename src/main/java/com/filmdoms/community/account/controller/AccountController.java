package com.filmdoms.community.account.controller;

import static com.filmdoms.community.account.data.constants.SessionKey.SESSION_KEY;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public Response<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto,
                                            HttpServletRequest request) {

        // 로그인 확인
        AccountDto accountDto = accountService.login(requestDto.getUsername(), requestDto.getPassword());

        // 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_KEY, accountDto.getUsername());

        // 응답 반환
        return Response.success(LoginResponseDto.from(accountDto));
    }

    // TODO: 회원 가입 기능 컨트롤러 작성
    @PostMapping("/join")
    public Response join() {
        AccountDto accountDto = accountService.join();
        return Response.success();
    }
}
