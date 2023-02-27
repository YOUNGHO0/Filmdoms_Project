package com.filmdoms.community.board.notice.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeMainPageDto;
import com.filmdoms.community.board.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/main-page")
    public Response<List<NoticeMainPageDto>> mainPageNotice() {
        List<NoticeMainPageDto> dtos = noticeService.getMainPageDtos();
        return Response.success(dtos);
    }

    @PostMapping
    public Response<NoticeCreateResponseDto> create(@RequestBody NoticeCreateRequestDto requestDto, @AuthenticationPrincipal AccountDto accountDto) {
        NoticeCreateResponseDto responseDto = noticeService.create(requestDto, accountDto);
        return Response.success(responseDto);
    }

    @PostMapping("/init-data")
    public Response initData() throws InterruptedException {
        noticeService.initData();
        return Response.success();
    }
}
