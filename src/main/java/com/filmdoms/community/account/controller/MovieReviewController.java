package com.filmdoms.community.account.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.dto.response.review.MovieReviewMainPageDto;
import com.filmdoms.community.account.service.MovieReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class MovieReviewController {

    private final MovieReviewService movieReviewService;

    @GetMapping("/main-page")
    public ResponseEntity<Response<List<MovieReviewMainPageDto>>> mainPageReview() {
        List<MovieReviewMainPageDto> dtos = movieReviewService.getMainPageDtos();
        return ResponseEntity.ok().body(Response.success(dtos));
    }

    @PostMapping("/init-data")
    public ResponseEntity<Response<Void>> initData() throws InterruptedException {
        movieReviewService.initData();
        return ResponseEntity.ok().body(Response.success());
    }
}
