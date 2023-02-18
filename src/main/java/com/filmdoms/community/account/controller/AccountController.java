package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public Response<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {

        // 로그인 확인
        String accessToken = accountService.login(requestDto.getUsername(), requestDto.getPassword());

        // JWT 토큰 반환
        return Response.success(new LoginResponseDto(accessToken));
    }

    // TODO: 회원 가입 기능 컨트롤러 작성
    @PostMapping("/join")
    public Response join() {
        AccountDto accountDto = accountService.join();
        return Response.success();
    }

    @GetMapping("/hello")
    public String test()
    {
        return "테스트에 성공하였습니다";
    }
}
