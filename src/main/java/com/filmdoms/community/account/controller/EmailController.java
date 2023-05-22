package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.dto.request.AuthCodeVerificationRequestDto;
import com.filmdoms.community.account.data.dto.request.SimpleEmailRequestDto;
import com.filmdoms.community.account.data.dto.response.EmailAuthDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/auth-code")
    public Response<Void> sendAuthCodeEmail(@RequestBody SimpleEmailRequestDto requestDto) {
        emailService.sendAuthCodeEmail(requestDto.getEmail());
        return Response.success();
    }

    @PostMapping("/auth-code/verification")
    public Response<EmailAuthDto> verifyAuthCode(@RequestBody AuthCodeVerificationRequestDto requestDto) {
        EmailAuthDto emailAuthDto = emailService.verifyAuthCode(requestDto);

        return Response.success(emailAuthDto);
    }

    @PostMapping("/temp-password")
    public Response<Void> sendTempPasswordEmail(@RequestBody SimpleEmailRequestDto requestDto) {
        emailService.sendTempPasswordEmail(requestDto.getEmail());
        return Response.success();
    }
}
