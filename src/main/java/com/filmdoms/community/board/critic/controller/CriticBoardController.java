package com.filmdoms.community.board.critic.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardGetResponseDto;
import com.filmdoms.community.board.critic.service.CriticBoardService;
import com.filmdoms.community.board.review.data.dto.request.post.MovieReviewPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/critic")
public class CriticBoardController {

    private final CriticBoardService criticBoardService;
    @PostMapping("/write")
    public Response<String> writeCritic(@RequestPart CriticBoardPostRequestDto criticBoardPostRequestDto, @RequestPart(required = false) List<MultipartFile> multipartFile )
    {

        return criticBoardService.writeCritic(criticBoardPostRequestDto,multipartFile);
    }

//    @GetMapping("/list")
//    public Response getCriticBoardList()
//    {
//
//        return Response()
//
//
//    }
}
