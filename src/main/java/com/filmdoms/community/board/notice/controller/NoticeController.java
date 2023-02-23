package com.filmdoms.community.board.notice.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeMainPageDto;
import com.filmdoms.community.board.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/create")
    public Response<NoticeCreateResponseDto> create(@RequestPart("data") NoticeCreateRequestDto requestDto, @RequestPart(value = "mainImage", required = false) MultipartFile mainImageFile, @RequestPart(value = "subImage", required = false) List<MultipartFile> subImageFiles) throws IOException {
        NoticeCreateResponseDto responseDto = noticeService.create(requestDto, mainImageFile, subImageFiles);
        return Response.success(responseDto);
    }

    @PostMapping("/init-data")
    public Response initData() throws InterruptedException {
        noticeService.initData();
        return Response.success();
    }
}
