package com.filmdoms.community.board.review.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.review.data.dto.request.MovieReviewCreateRequestDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewCreateResponseDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewMainPageDto;
import com.filmdoms.community.board.review.service.MovieReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class MovieReviewController {

    private final MovieReviewService movieReviewService;

    @GetMapping("/main-page")
    public Response<List<MovieReviewMainPageDto>> mainPageReview() {
        List<MovieReviewMainPageDto> dtos = movieReviewService.getMainPageDtos();
        return Response.success(dtos);
    }

    @PostMapping
    public Response<MovieReviewCreateResponseDto> create(@RequestBody MovieReviewCreateRequestDto requestDto, @AuthenticationPrincipal AccountDto accountDto) {
        MovieReviewCreateResponseDto responseDto = movieReviewService.create(requestDto, accountDto);
        return Response.success(responseDto);
    }

    @PostMapping("/init-data")
    public Response initData() throws InterruptedException {
        movieReviewService.initData();
        return Response.success();
    }

    @PostMapping("/test")
    public Response test(@RequestPart("image") MultipartFile image) {
        log.info(image.getContentType());
        log.info(image.getName());
        log.info(image.getOriginalFilename());
        return Response.success();
    }
}
