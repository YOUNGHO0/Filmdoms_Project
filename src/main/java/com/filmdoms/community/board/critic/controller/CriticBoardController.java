package com.filmdoms.community.board.critic.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.service.CriticBoardService;
import com.filmdoms.community.board.review.data.dto.request.post.MovieReviewPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/critic")
public class CriticBoardController {

    private final CriticBoardService criticBoardService;
    @PostMapping("/write")
    public Response<String> writeReview(@RequestPart CriticBoardPostRequestDto criticBoardPostRequestDto, @RequestPart(required = false) MultipartFile multipartFile )
    {

        return criticBoardService.writeMovieReview(criticBoardPostRequestDto,multipartFile);
    }
}
