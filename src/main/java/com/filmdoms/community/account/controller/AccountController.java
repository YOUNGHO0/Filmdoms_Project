package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.LoginRequestDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.service.AccountService;
import com.filmdoms.community.account.service.AmazonS3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AmazonS3Upload amazonS3Upload;

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


    @PostMapping("/api/v1/fileupload")
    public Response<String> uploadFile(@RequestParam("images") MultipartFile multipartFile) throws IOException {

        return Response.success(amazonS3Upload.upload(multipartFile)) ;

    }

}
