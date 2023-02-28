package com.filmdoms.community.board.notice.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.request.NoticeUpdateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeDetailResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeMainPageDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeUpdateResponseDto;
import com.filmdoms.community.board.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{noticeId}")
    public Response<NoticeDetailResponseDto> detail(@PathVariable Long noticeId) {
        NoticeDetailResponseDto responseDto = noticeService.getDetail(noticeId);
        return Response.success(responseDto);
    }

    @DeleteMapping("/{noticeId}")
    public Response delete(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return Response.success();
    }

    @PutMapping("/{noticeId}")
    public Response<NoticeUpdateResponseDto> update(@PathVariable Long noticeId, @RequestBody NoticeUpdateRequestDto requestDto) {
        log.info("{}", requestDto.getContentImageId());
        NoticeUpdateResponseDto responseDto = noticeService.updateNotice(noticeId, requestDto);
        return Response.success(responseDto);
    }

    @PostMapping("/init-data")
    public Response initData() throws InterruptedException {
        noticeService.initData();
        return Response.success();
    }
}
