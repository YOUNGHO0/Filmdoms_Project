package com.filmdoms.community.board.review.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.review.data.dto.request.post.MovieReviewPostDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewMainPageDto;
import com.filmdoms.community.board.review.service.MovieReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
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

    @PostMapping("/write")
    public Response<String> writeReview(@RequestPart MovieReviewPostDto movieReviewPostDto, @RequestPart(required = false) MultipartFile multipartFile )
    {

     return movieReviewService.writeMovieReview(movieReviewPostDto,multipartFile);
    }


}
